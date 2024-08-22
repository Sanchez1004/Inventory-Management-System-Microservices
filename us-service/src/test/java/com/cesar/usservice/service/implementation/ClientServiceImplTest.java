package com.cesar.usservice.service.implementation;

import com.cesar.usservice.dto.ClientDTO;
import com.cesar.usservice.dto.OrderDetailsDTO;
import com.cesar.usservice.dto.mapper.ClientMapper;
import com.cesar.usservice.entity.ClientEntity;
import com.cesar.usservice.repository.ClientRepository;
import com.cesar.usservice.utils.Address;
import com.cesar.usservice.utils.OrderStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Spy
    private final ClientMapper clientMapper = new ClientMapper();

    @InjectMocks
    private ClientServiceImpl clientService;

    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImplTest.class);
    private ClientDTO clientDTO;
    private ClientEntity clientEntity;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        List<OrderDetailsDTO> ordersList = new ArrayList<>();
        Address address = Address.builder()
                .city("City")
                .houseNumber("123")
                .neighborhood("Neighborhood")
                .state("State")
                .streetName("456")
                .build();

        ordersList.add(OrderDetailsDTO.builder()
                .id("1")
                .orderStatus(OrderStatus.PENDING)
                .orderTotal(100.0)
                .build());

        clientEntity = ClientEntity.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address(address)
                .pendingOrders(ordersList)
                .build();

        clientDTO = ClientDTO.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address(address)
                .pendingOrders(ordersList)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void saveClient_Succeed() {
        logger.info("Client Entity: {}", clientEntity);
        logger.info("Client DTO:    {}", clientDTO);

        when(clientRepository.findById(clientEntity.getId())).thenReturn(Optional.empty());
        when(clientMapper.toEntity(clientDTO)).thenReturn(clientEntity);
        when(clientRepository.save(clientEntity)).thenReturn(clientEntity);
        when(clientMapper.toDTO(clientEntity)).thenReturn(clientDTO);

        ClientDTO savedClient = clientService.saveClient(clientDTO);

        assertNotNull(savedClient);
    }
}
