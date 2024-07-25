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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
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
            if (request.getClientId() != null) {
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
                    entity.setClientEmail(clientDTO.getEmail());
                }
            }
        });

        updateFieldMap.put(OrderField.ITEM_LIST, (entity, request) -> {
            if (request.getItemList() != null) {
                adjustOrder(entity, request);
            }
        });
    }

    private void adjustOrder(OrderEntity entity, OrderDTO request) {
        Map<String, Integer> availableItems = new LinkedHashMap<>();
        Map<String, Integer> failedItems = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> entry : request.getItemList().entrySet()) {
            String itemId = entry.getKey();
            Integer quantity = entry.getValue();

            if (Boolean.TRUE.equals(inventoryServiceClient.isItemAvailable(itemId, quantity).getBody())) {
                availableItems.put(itemId, quantity);
            } else {
                failedItems.put("Not enough stock for item: " + itemId, quantity);
            }
        }

        if (failedItems.isEmpty()) {
            for (Map.Entry<String, Integer> entry : availableItems.entrySet()) {
                String itemId = entry.getKey();
                Integer quantity = entry.getValue();

                Integer oldQuantity = entity.getItemList().get(itemId);
                handleAvailableItemsList(entity, itemId, quantity, oldQuantity);
            }
        } else {
            entity.setItemList(failedItems);
            entity.setOrderStatus(OrderStatus.ERROR);
        }
    }

    private void handleAvailableItemsList(OrderEntity entity, String itemId, Integer quantity, Integer oldQuantity) {
        if (entity.getItemList().containsKey(itemId)) {
            if (oldQuantity < quantity) {
                if (Boolean.TRUE.equals(inventoryServiceClient.deductItemById(itemId, quantity - oldQuantity).getBody())){
                    entity.getItemList().put(itemId, quantity);
                }
            }
            else if (oldQuantity > quantity) {
                if (Boolean.TRUE.equals(inventoryServiceClient.addStockToItem(itemId, oldQuantity - quantity).getBody())) {
                    entity.getItemList().put(itemId, quantity);
                }
            }
            else {
                if (Boolean.TRUE.equals(inventoryServiceClient.deductItemById(itemId, quantity).getBody())) {
                    entity.getItemList().put(itemId, quantity);
                }
            }
        }
    }

    @Override
    public OrderDTO getOrderById(String id) {
        return orderMapper.toDTO(orderRepository.findOrderEntityById(id)
                .orElseThrow(() -> new OrderException("No order found with id: " + id)));
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        if (!orderDTO.getClientId().isEmpty()) {
            ClientDTO clientDTO = userServiceClient.getClientById(orderDTO.getClientId()).getBody();

            if (clientDTO != null && !clientDTO.getFirstName().isEmpty() && !clientDTO.getLastName().isEmpty() && !orderDTO.getItemList().isEmpty()) {
                Map<String, Integer> itemListResponse = inventoryServiceClient.deductItemsById(orderDTO.getItemList()).getBody();

                if (itemListResponse != null && !itemListResponse.isEmpty()) {
                    return handleListResponse(itemListResponse, clientDTO);
                }
            }
            logger.error("Client not found");
            logger.error("The client name, email and order item list is required!");
            throw new OrderException("Client not found");
        }
        logger.error("Client id is required");
        throw new OrderException("Client id is required");
    }

    private OrderDTO handleListResponse(Map<String, Integer> itemListResponse, ClientDTO clientDTO) {
        Map<String, Integer> outOfStockItems = new LinkedHashMap<>();
        Map<String, Integer> deductedItems = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> entry : itemListResponse.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            if (key.contains(" - ")) {
                String[] parts = key.split(" - ");
                String id = parts[0];
                String name = parts[1];

                logger.info("Item with id: '{}' and name: '{}', it's out of stock, actual stock: '{}'", id, name, value);
                outOfStockItems.put(key, value);
            }
            else {
                deductedItems.put(key, value);
            }
        }
        if (!outOfStockItems.isEmpty()) {
            return OrderDTO.builder()
                    .orderStatus(OrderStatus.ERROR)
                    .itemList(outOfStockItems)
                    .build();
        }

        if (!deductedItems.isEmpty()) {
            Double orderTotal = inventoryServiceClient.getItemListTotal(deductedItems).getBody();
            if (orderTotal != null && orderTotal > 0) {
                OrderEntity orderEntity = OrderEntity.builder()
                        .date(LocalDateTime.now())
                        .orderStatus(OrderStatus.CREATED)
                        .itemList(deductedItems)
                        .orderTotal(orderTotal)
                        .clientId(clientDTO.getId())
                        .clientFirstName(clientDTO.getFirstName())
                        .clientLastName(clientDTO.getLastName())
                        .clientEmail(clientDTO.getEmail())
                        .build();

                OrderDTO orderCreated = orderMapper.toDTO(orderRepository.save(orderEntity));
                updateUserOrders(orderCreated);
                return orderCreated;
            }
            logger.error("Order total cannot be 0, check if the client is working");
            throw new OrderException("Order total cannot be empty!");
        }
        throw new OrderException("There was an error handling the item list response");
    }

    private void updateUserOrders(OrderDTO orderDTO) {
        OrderDetailsDTO orderDetailsDTO = OrderDetailsDTO.builder()
                .id(orderDTO.getId())
                .orderStatus(orderDTO.getOrderStatus())
                .orderTotal(orderDTO.getOrderTotal())
                .build();

        userServiceClient.updateClientOrdersById(orderDetailsDTO, orderDTO.getClientId());
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

        if (orderEntity.getOrderStatus() == OrderStatus.ERROR) {
            return orderMapper.toDTO(orderEntity);
        }
        return orderMapper.toDTO(orderRepository.save(orderEntity));
    }

    @Override
    public String deleteOrderById(String orderId) {
        OrderEntity orderEntity = findOrderById(orderId);
        if (orderEntity != null) {
            orderRepository.delete(orderEntity);
            return "Order with id: " + orderId + " deleted successfully";
        }
        return "Order with id: " + orderId + " not found";
    }

    private OrderEntity findOrderById(String id) {
        Optional<OrderEntity> orderEntity = orderRepository.findOrderEntityById(id);
        return orderEntity.orElse(null);
    }
}
