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
import com.cesar.ordservice.utils.OrderStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserServiceClient userServiceClient;
    private final InventoryServiceClient inventoryServiceClient;

    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, UserServiceClient userServiceClient, InventoryServiceClient inventoryServiceClient) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userServiceClient = userServiceClient;
        this.inventoryServiceClient = inventoryServiceClient;
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

            if (addedItemList != null && !addedItemList.isEmpty()) {
                Double orderTotal = inventoryServiceClient.getItemListTotal(addedItemList).getBody();
                if (orderTotal != null) {
                    OrderEntity orderEntity = OrderEntity.builder()
                            .date(LocalDateTime.now())
                            .orderStatus(OrderStatus.CREATED)
                            .itemList(addedItemList)
                            .orderTotal(orderTotal)
                            .clientId(orderDTO.getClientId())
                            .clientName(orderDTO.getClientName())
                            .clientMail(orderDTO.getClientMail())
                            .build();

                    userServiceClient.updateClientOrdersById(OrderDetailsDTO.builder()
                                    .orderStatus(orderEntity.getOrderStatus()).orderTotal(orderEntity.getOrderTotal()).build(),
                            clientDTO.getId());

                    return orderMapper.toDTO(orderEntity);
                }
                throw new OrderException("Order total cannot be empty!");
            }

        }
        logger.error("Item list cannot be empty!");
        throw new OrderException("Item list cannot be empty!");
    }
}
