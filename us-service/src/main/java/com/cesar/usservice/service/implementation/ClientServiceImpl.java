package com.cesar.usservice.service.implementation;

import com.cesar.usservice.dto.ClientDTO;
import com.cesar.usservice.dto.mapper.ClientMapper;
import com.cesar.usservice.entity.ClientEntity;
import com.cesar.usservice.dto.OrderDetailsDTO;
import com.cesar.usservice.repository.ClientRepository;
import com.cesar.usservice.service.ClientService;
import com.cesar.usservice.exception.ClientException;
import com.cesar.usservice.utils.ClientField;
import com.cesar.usservice.utils.OrderStatus;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Implementation of the ClientService interface, managing business logic for client operations.
 */
@Service
public class ClientServiceImpl implements ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final Map<ClientField, BiConsumer<ClientEntity, ClientDTO>> updateFieldMap = new EnumMap<>(ClientField.class);

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        initializeUpdateFieldMap();
    }

    /**
     * Initializes the map that defines how to update each field of a client.
     */
    private void initializeUpdateFieldMap() {
        updateFieldMap.put(ClientField.ID, (entity, request) -> {
            if (request.getId() != null) {
                entity.setId(request.getId());
            }
        });
        updateFieldMap.put(ClientField.FIRST_NAME, (entity, request) -> {
            if (request.getFirstName() != null) {
                entity.setFirstName(request.getFirstName());
            }
        });
        updateFieldMap.put(ClientField.LAST_NAME, (entity, request) -> {
            if (request.getLastName() != null) {
                entity.setLastName(request.getLastName());
            }
        });
        updateFieldMap.put(ClientField.EMAIL, (entity, request) -> {
            if (request.getEmail() != null) {
                entity.setEmail(request.getEmail());
            }
        });
        updateFieldMap.put(ClientField.ADDRESS, (entity, request) -> {
            if (request.getAddress() != null) {
                entity.setAddress(request.getAddress());
            }
        });
        updateFieldMap.put(ClientField.ORDER_DETAILS, (entity, request) -> {
            if (request.getPendingOrders() != null) {
                List<OrderDetailsDTO> orderList = entity.getPendingOrders();
                OrderDetailsDTO newOrderDetails = request.getPendingOrders().get(0);
                updateItemList(orderList, newOrderDetails, entity);
            }
        });
    }

    /**
     * Updates the order list with new order details.
     *
     * @param orderList       the current list of orders
     * @param newOrderDetails the new order details to update or add
     * @param entity          the client entity being updated
     */
    private void updateItemList(List<OrderDetailsDTO> orderList, OrderDetailsDTO newOrderDetails, ClientEntity entity) {
        if (orderList != null) {
            if (orderList.isEmpty()) {
                orderList.add(newOrderDetails);
            } else {
                boolean orderUpdated = false;
                for (OrderDetailsDTO order : orderList) {
                    if (Objects.equals(order.getId(), newOrderDetails.getId())) {
                        order.setOrderStatus(newOrderDetails.getOrderStatus());
                        order.setOrderTotal(newOrderDetails.getOrderTotal());
                        orderUpdated = true;
                        break;
                    }
                }
                if (!orderUpdated) {
                    orderList.add(newOrderDetails);
                }
            }
            entity.setPendingOrders(orderList);
        }
    }

    /**
     * Retrieves a client by their unique ID.
     *
     * @param id the unique identifier of the client to be retrieved
     * @return the `ClientDTO` object representing the client with the specified ID
     * @throws ClientException if no client is found with the given ID
     */
    @Override
    public ClientDTO getClientById(String id) {
        return clientMapper.toDTO(clientRepository.findClientEntityById(id)
                .orElseThrow(() -> new ClientException("Client not found with id: " + id)));
    }


    /**
     * Retrieves a list of clients whose details match the provided keyword.
     *
     * @param keyword the search keyword used to find matching clients
     * @return a list of `ClientDTO` objects that match the search keyword
     * @throws ClientException if the keyword is empty
     */
    @Override
    public List<ClientDTO> getListOfClientsByKeyword(String keyword) {
        if (keyword.isEmpty()) {
            logger.error("Keyword cannot be empty!");
            throw new ClientException("Keyword cannot be empty!");
        }

        // Search for clients matching the keyword and map results to DTOs
        return clientRepository.findClientsByKeyword(keyword)
                .stream().map(clientMapper::toDTO)
                .toList();
    }


    /**
     * Retrieves a client associated with a specific order ID.
     *
     * @param orderId the unique identifier of the order
     * @return the `ClientDTO` associated with the given order ID
     * @throws ClientException if the order ID is empty or if no client is found with the provided order ID
     */
    @Override
    public ClientDTO getClientByOrderId(String orderId) {
        if (orderId.isEmpty()) {
            logger.error("Order ID cannot be empty!");
            throw new ClientException("Order ID cannot be empty");
        }

        // Search for the client associated with the given order ID
        return clientMapper.toDTO(clientRepository.findByOrderDetailsId(orderId)
                .orElseThrow(() -> new ClientException("No client found with order ID: " + orderId)));
    }


    /**
     * Saves a new client to the repository after validating the provided `ClientDTO`.
     *
     * @param clientDTO the data transfer object containing the client's information
     * @return the saved client information as a DTO
     * @throws ClientException if the provided `ClientDTO` is null, if basic fields are empty, or if a client with the same ID already exists
     */
    @Override
    public ClientDTO saveClient(ClientDTO clientDTO) {
        if (clientDTO == null) {
            throw new ClientException("ClientDTO cannot be null");
        }
        if (clientDTO.getFirstName().isEmpty() || clientDTO.getEmail().isEmpty() || clientDTO.getId().isEmpty()) {
            logger.error("Basic fields of new client are empty");
            throw new ClientException("Client ID, email, and first name cannot be empty");
        }
        if (clientRepository.findById(clientDTO.getId()).isPresent()) {
            logger.info("Client with ID '{}' already exists", clientDTO.getId());
            throw new ClientException("Client already exists");
        }

        // Convert the DTO to an entity for saving to the database
        ClientEntity newClient = clientMapper.toEntity(clientDTO);

        // Save the new client entity to the repository
        ClientEntity createdClient = clientRepository.save(newClient);

        // Convert the saved entity back to a DTO to return to the caller
        return clientMapper.toDTO(createdClient);
    }

    /**
     * Updates the client's information based on the provided `ClientDTO` and client ID.
     *
     * @param clientDTO the data transfer object containing the updated client information
     * @param id the ID of the client to update
     * @return the updated client information as a DTO
     * @throws ClientException if the provided `ClientDTO` is null or if the client with the specified ID is not found
     */
    @Override
    public ClientDTO updateClientById(ClientDTO clientDTO, String id) {
        if (clientDTO == null) {
            logger.error("ClientDTO cannot be null");
            throw new ClientException("Client request cannot be null");
        }

        // Retrieve the existing client entity by ID
        ClientEntity clientEntity = clientMapper.toEntity(getClientById(id));

        // Iterate through the updateFieldMap and apply updates to the client entity
        for (Map.Entry<ClientField, BiConsumer<ClientEntity, ClientDTO>> entry : updateFieldMap.entrySet()) {
            entry.getValue().accept(clientEntity, clientDTO);
        }

        // Save the updated client entity and return the corresponding DTO
        return clientMapper.toDTO(clientRepository.save(clientEntity));
    }

    /**
     * Updates the list of orders for a client with the provided order details.
     *
     * @param orderDetailsDTO the new order details to be added to the client's list of pending orders
     * @param id the ID of the client whose orders are to be updated
     * @return the updated client information as a DTO
     * @throws ClientException if the provided order details are null
     */
    @Override
    public ClientDTO updateClientOrdersByClientId(OrderDetailsDTO orderDetailsDTO, String id) {
        if (orderDetailsDTO == null) {
            logger.error("OrderDetailsDTO cannot be null");
            throw new ClientException("Order details cannot be empty");
        }

        // Create a new list with the provided order details
        List<OrderDetailsDTO> newOrder = new ArrayList<>();
        newOrder.add(orderDetailsDTO);

        // Retrieve the client entity from the database using the client ID
        ClientEntity clientEntity = clientMapper.toEntity(getClientById(id));

        // Create a ClientDTO with the new list of orders
        ClientDTO clientDTO = ClientDTO.builder()
                .pendingOrders(newOrder)
                .build();

        // Use a BiConsumer to update the order details in the client entity
        BiConsumer<ClientEntity, ClientDTO> updateOrderDetails = updateFieldMap.get(ClientField.ORDER_DETAILS);
        updateOrderDetails.accept(clientEntity, clientDTO);

        // Save the updated client entity to the repository and return the updated DTO
        return clientMapper.toDTO(clientRepository.save(clientEntity));
    }


    /**
     * Updates the status of a specific order for a client based on the provided order ID and client ID.
     *
     * @param orderId the ID of the order whose status is to be updated
     * @param newOrderStatus the new status to be set for the order
     * @param clientId the ID of the client whose order status is being updated
     * @return the updated client information as a DTO
     */
    @Override
    public ClientDTO updateClientOrderStatusByClientId(String orderId, OrderStatus newOrderStatus, String clientId) {
        // Convert the ClientDTO retrieved using the client ID to a ClientEntity
        ClientEntity clientEntity = clientMapper.toEntity(getClientById(clientId));

        // Retrieve the list of pending orders for the client
        List<OrderDetailsDTO> orderDetailsDTOList = clientEntity.getPendingOrders();

        // Iterate over the list to find the order with the given order ID
        for (OrderDetailsDTO orderDetailsDTO : orderDetailsDTOList) {
            if (Objects.equals(orderDetailsDTO.getId(), orderId)) {
                // Update the status of the found order
                orderDetailsDTO.setOrderStatus(newOrderStatus);
                break;
            }
        }

        // Save the updated client entity to the repository and return the updated client information as a DTO
        return clientMapper.toDTO(clientRepository.save(clientEntity));
    }


    /**
     * Deletes a client from the repository based on the provided client ID.
     *
     * @param id the ID of the client to be deleted
     * @return a message indicating whether the client was successfully deleted or not found
     */
    @Override
    public String deleteClientById(String id) {
        ClientEntity clientEntity = clientMapper.toEntity(getClientById(id));
        if (clientEntity != null) {
            clientRepository.delete(clientEntity);
            return "Client with ID: '" + id + "' was successfully deleted";
        }
        logger.info("Client with ID: '{}' not found", id);
        return "Client with ID: '" + id + "' not found";
    }


    /**
     * Deletes a specific order from the client's pending orders list.
     *
     * @param clientId the ID of the client from which to remove the order
     * @param orderId the ID of the order to be removed
     * @return a message indicating whether the order was successfully deleted or not found
     */
    @Override
    public String deleteOrderDetailsById(String clientId, String orderId) {
        ClientEntity clientEntity = clientRepository.findClientEntityById(clientId).orElse(null);

        if (clientEntity == null) {
            return "Client not found";
        }

        List<OrderDetailsDTO> orderDetailsList = clientEntity.getPendingOrders();
        if (orderDetailsList.removeIf(orderDetailsDTO -> Objects.equals(orderDetailsDTO.getId(), orderId))) {
            clientEntity.setPendingOrders(orderDetailsList);
            clientRepository.save(clientEntity);
            return "Order with ID: '" + orderId + "' was successfully deleted";
        }

        return "Order with ID: '" + orderId + "' not found";
    }

}
