package com.cesar.ordservice.service.implementation;

import com.cesar.ordservice.client.InventoryServiceClient;
import com.cesar.ordservice.client.UserServiceClient;
import com.cesar.ordservice.dto.ClientDTO;
import com.cesar.ordservice.dto.OrderDTO;
import com.cesar.ordservice.dto.OrderDetailsDTO;
import com.cesar.ordservice.dto.mapper.OrderMapper;
import com.cesar.ordservice.entity.OrderEntity;
import com.cesar.ordservice.exception.OrderException;
import com.cesar.ordservice.repository.OrderRepository;
import com.cesar.ordservice.service.OrderService;
import com.cesar.ordservice.utils.OrderField;
import com.cesar.ordservice.utils.OrderStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserServiceClient userServiceClient;
    private final InventoryServiceClient inventoryServiceClient;

    private final Map<OrderField, BiConsumer<OrderEntity, OrderDTO>> updateFieldMap = new EnumMap<>(OrderField.class);


    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, UserServiceClient userServiceClient, InventoryServiceClient inventoryServiceClient) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userServiceClient = userServiceClient;
        this.inventoryServiceClient = inventoryServiceClient;

        initializeUpdateFieldMap();
    }

    private void initializeUpdateFieldMap() {
        updateFieldMap.put(OrderField.ORDER_STATUS, (entity, request) -> {
            OrderStatus[] orderStatuses = OrderStatus.values();
            for (OrderStatus status : orderStatuses) {
                if (status == request.getOrderStatus()) {
                    entity.setOrderStatus(status);
                    userServiceClient.updateClientOrderStatusById(entity.getId(), status, entity.getClientId());
                    break;
                }
            }
        });
        updateFieldMap.put(OrderField.CLIENT_ID, (entity, request) -> {
            ClientDTO clientDTO = userServiceClient.getClientById(request.getClientId()).getBody();
            if (clientDTO != null) {
                userServiceClient.deleteClientOrderByOrderId(entity.getClientId(), entity.getId());

                userServiceClient.updateClientOrdersById(
                        OrderDetailsDTO.builder()
                                .orderTotal(entity.getOrderTotal())
                                .orderStatus(entity.getOrderStatus())
                                .id(entity.getId())
                                .build(),
                        clientDTO.getId());

                entity.setClientId(clientDTO.getId());
                entity.setClientFirstName(clientDTO.getFirstName());
                entity.setClientLastName(clientDTO.getLastName());
                entity.setClientMail(clientDTO.getMail());
            }
        });
        updateFieldMap.put(OrderField.ITEM_LIST, (entity, request) -> {
              for (Map.Entry<String, Integer> entry : request.getItemList().entrySet()) {
                  String itemId = entry.getKey();
                  Integer quantity = entry.getValue();

                  if (entity.getItemList().containsKey(itemId)) {
                      Integer oldItemQuantity = entity.getItemList().get(itemId);
                      if (oldItemQuantity > quantity && Boolean.TRUE.equals(inventoryServiceClient.addStockToItem(itemId,
                              oldItemQuantity - quantity).getBody())) {
                          entity.getItemList().put(itemId, oldItemQuantity - quantity);
                      }
                      if (oldItemQuantity < quantity && Boolean.TRUE.equals(inventoryServiceClient.deductItemById(itemId,
                              quantity - oldItemQuantity).getBody())) {
                              entity.getItemList().put(itemId, quantity - oldItemQuantity);
                      }
                  }
                  if (Boolean.TRUE.equals(inventoryServiceClient.deductItemById(itemId, quantity).getBody())) {
                      entity.getItemList().put(itemId, quantity);
                  }
              }
        });
    }

    @Override
    public OrderDTO getOrderById(String id) {
        return orderMapper.toDTO(orderRepository.findOrderEntityById(id)
                .orElseThrow(() -> new OrderException("No order found with id: " + id)));
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        if (orderDTO.getClientId().isEmpty()) {
            logger.error("Client id is required");
            throw new OrderException("Client id is required");
        }
        ClientDTO clientDTO = userServiceClient.getClientById(orderDTO.getClientId()).getBody();
        if (clientDTO == null) {
            logger.error("Client not found");
            throw new OrderException("Client not found");
        }

        if (!clientDTO.getFirstName().isEmpty() && !clientDTO.getLastName().isEmpty() && !orderDTO.getItemList().isEmpty()) {
            Map<String, Integer> addedItemList = inventoryServiceClient.deductItemsById(orderDTO.getItemList()).getBody();

            if (addedItemList != null && addedItemList.size() != orderDTO.getItemList().size()) {
                return OrderDTO.builder()
                        .orderStatus(OrderStatus.ERROR)
                        .itemList(addedItemList)
                        .build();
            }

            if (addedItemList != null && !addedItemList.isEmpty()) {
                Double orderTotal = inventoryServiceClient.getItemListTotal(addedItemList).getBody();
                if (orderTotal != null) {
                    OrderEntity orderEntity = OrderEntity.builder()
                            .date(LocalDateTime.now())
                            .orderStatus(OrderStatus.CREATED)
                            .itemList(addedItemList)
                            .orderTotal(orderTotal)
                            .clientId(orderDTO.getClientId())
                            .clientFirstName(orderDTO.getClientFirstName())
                            .clientLastName(orderDTO.getClientLastName())
                            .clientMail(orderDTO.getClientMail())
                            .build();

                    userServiceClient.updateClientOrdersById(OrderDetailsDTO.builder()
                                    .orderStatus(orderEntity.getOrderStatus()).orderTotal(orderEntity.getOrderTotal()).build(),
                            clientDTO.getId());

                    return orderMapper.toDTO(orderEntity);
                }
                logger.error("Order total cannot be 0, check if the client is working");
                throw new OrderException("Order total cannot be empty!");
            }
            logger.error("The client name, email and order item list is required!");
            throw new OrderException("The client name, email and order item list is required!");
        }
        logger.error("Item list cannot be empty!");
        throw new OrderException("Item list cannot be empty!");
    }

    @Override
    public OrderDTO updateOrder(OrderDTO orderDTO) {
        OrderEntity orderEntity = orderMapper.toEntity(getOrderById(orderDTO.getId()));
        if (orderEntity == null) {
            logger.info("The order doesn't exist with id: {}", orderDTO.getId());
            throw new OrderException("The order doesn't exist");
        }

        for (Map.Entry<OrderField, BiConsumer<OrderEntity, OrderDTO>> entry : updateFieldMap.entrySet()) {
            entry.getValue().accept(orderEntity, orderDTO);
        }

        return orderMapper.toDTO(orderRepository.save(orderEntity));
    }

    @Override
    public String deleteOrderById(String orderId) {
        OrderEntity orderEntity = orderMapper.toEntity(getOrderById(orderId));
        if (orderEntity != null) {
            orderRepository.delete(orderEntity);
            return "Order with id: " + orderId + " deleted successfully";
        }
        return "Order with id: " + orderId + " deleted successfully";
    }
}
