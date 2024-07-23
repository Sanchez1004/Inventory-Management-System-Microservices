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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

@Service
public class ClientServiceImpl implements ClientService {

    private static final Logger logger = LogManager.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final Map<ClientField, BiConsumer<ClientEntity, ClientDTO>> updateFieldMap = new EnumMap<>(ClientField.class);

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;

        initializeUpdateFieldMap();
    }

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
            List<OrderDetailsDTO> orderList = entity.getPendingOrders();
            OrderDetailsDTO newOrderDetails = request.getPendingOrders().getFirst();

            if (orderList == null) {
                orderList = new ArrayList<>();
                orderList.add(newOrderDetails);
                entity.setPendingOrders(orderList);
                return;
            }

            if (orderExist(orderList, newOrderDetails)) {
                entity.setPendingOrders(orderList);
            }
            else {
                orderList.add(newOrderDetails);
                entity.setPendingOrders(orderList);
            }
        });
    }

    private boolean orderExist(List<OrderDetailsDTO> orderList, OrderDetailsDTO newOrderDetails) {
        for (OrderDetailsDTO order : orderList) {
            if (order.getId().equals(newOrderDetails.getId())) {
                orderList.set(orderList.indexOf(order), newOrderDetails);
                return true;
            }
        }
        return false;
    }


    @Override
    public ClientDTO getClientById(String id) {
        return clientMapper.toDTO(clientRepository.findClientEntityById(id)
                .orElseThrow(() -> new ClientException("User not found with id: " + id)));
    }

    @Override
    public List<ClientDTO> getListOfClientsByKeyword(String keyword) {
        if (!keyword.isEmpty()) {
            return clientRepository.findClientsByKeyword(keyword)
                    .stream().map(clientMapper::toDTO)
                    .toList();
        }
        logger.error("Keyword cannot be empty!");
        throw new ClientException("keyword cannot be empty!");
    }

    @Override
    public ClientDTO getClientByOrderId(String orderId) {
        if (orderId.isEmpty()) {
            logger.error("Order id cannot be empty!");
            throw new ClientException("Order id cannot be Empty");
        }

        return clientMapper.toDTO(clientRepository.findByOrderDetailsId(orderId)
                .orElseThrow(() -> new ClientException("No client found with that order id, ORDER ID: " + orderId)));
    }

    @Override
    public ClientDTO saveClient(ClientDTO clientDTO) {
        if (clientDTO.getId() != null && clientDTO.getFirstName() != null && clientDTO.getEmail() != null) {
            if (clientRepository.findById(clientDTO.getId()).isPresent()) {
                logger.info("User with id : {} already exists", clientDTO.getId());
                throw new ClientException("User already exists");
            }
            return clientMapper.toDTO(clientRepository.save(clientMapper.toEntity(clientDTO)));
        }
        logger.error("Basic fields of new client are empty");
        throw new ClientException("User id, email and first name cannot be empty");
    }

    @Override
    public ClientDTO updateClientById(ClientDTO clientDTO, String id) {
        if (clientDTO == null) {
            logger.error("At least one attribute has to not be null");
            throw new ClientException("User request cannot be null");
        }

        ClientEntity clientEntity = clientMapper.toEntity(getClientById(id));

        for (Map.Entry<ClientField, BiConsumer<ClientEntity, ClientDTO>> entry : updateFieldMap.entrySet()) {
            entry.getValue().accept(clientEntity, clientDTO);
        }

        return clientMapper.toDTO(clientRepository.save(clientEntity));
    }

    @Override
    public ClientDTO updateClientOrdersByClientId(OrderDetailsDTO orderDetailsDTO, String id) {
        if (orderDetailsDTO == null) {
            logger.error("User details cannot be null, every field must be full");
            throw new ClientException("The order details cannot be empty");
        }
        ClientEntity clientEntity = clientMapper.toEntity(getClientById(id));
        ClientDTO clientDTO = ClientDTO.builder()
                .pendingOrders(List.of(orderDetailsDTO))
                .build();

        BiConsumer<ClientEntity, ClientDTO> updateOrderDetails = updateFieldMap.get(ClientField.ORDER_DETAILS);
        updateOrderDetails.accept(clientEntity, clientDTO);

        return clientMapper.toDTO(clientRepository.save(clientEntity));
    }

    @Override
    public ClientDTO updateClientOrderStatusByClientId(String orderId, OrderStatus newOrderStatus, String clientId) {
        ClientEntity clientEntity = clientMapper.toEntity(getClientById(clientId));

        List<OrderDetailsDTO> orderDetailsDTOList = clientEntity.getPendingOrders();
        for (OrderDetailsDTO orderDetailsDTO : orderDetailsDTOList) {
            if (Objects.equals(orderDetailsDTO.getId(), orderId)) {
                orderDetailsDTO.setOrderStatus(newOrderStatus);
                break;
            }
        }
        return clientMapper.toDTO(clientEntity);
    }

    @Override
    public String deleteClientById(String id) {
        ClientEntity clientEntity = clientMapper.toEntity(getClientById(id));
        if (clientEntity != null) {
            clientRepository.delete(clientEntity);
            return "Client with id: " + id + " was successfully deleted";
        }
        logger.info("Client with id: {} not found", id);
        throw new ClientException("Client with id: " + id + " not found");
    }
}
