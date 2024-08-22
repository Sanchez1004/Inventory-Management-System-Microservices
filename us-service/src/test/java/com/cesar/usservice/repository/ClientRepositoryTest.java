package com.cesar.usservice.repository;

import com.cesar.usservice.dto.OrderDetailsDTO;
import com.cesar.usservice.entity.ClientEntity;
import com.cesar.usservice.utils.Address;
import com.cesar.usservice.utils.OrderStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@DataMongoTest
@ExtendWith(MockitoExtension.class)
class ClientRepositoryTest {

    @Mock
    private ClientRepository clientRepository;

    private AutoCloseable closeable;
    private ClientEntity clientEntity;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        // Arrange

        List<OrderDetailsDTO> pendingOrders = new ArrayList<>();
        pendingOrders.add(OrderDetailsDTO.builder().id("1").orderStatus(OrderStatus.PENDING).orderTotal(999).build());
        clientEntity = ClientEntity.builder()
                .id("1")
                .email("test@email")
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .pendingOrders(pendingOrders)
                .address(Address.builder().city("City").houseNumber("123").neighborhood("Neighborhood").state("State").streetName("456").build())
                .build();
        clientRepository.save(clientEntity);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void findClientEntityById_Found() {
        when(clientRepository.findClientEntityById("1")).thenReturn(Optional.of(clientEntity));

        Optional<ClientEntity> existentClient = clientRepository.findClientEntityById("1");

        assertNotNull(existentClient, "User name: " + existentClient.get().getFirstName());
        existentClient.ifPresent(entity -> assertEquals("TestFirstName", entity.getFirstName()));
    }

    @Test
    void findClientsByKeyword() {
        String keyword = "lAsT";
        String lowerCaseKeyword = keyword.toLowerCase();
        when(clientRepository.findClientsByKeyword(keyword)).thenReturn(List.of(clientEntity));

        List<ClientEntity> clients = clientRepository.findClientsByKeyword(keyword);

        assertNotNull(clients);
        assertTrue(clients.stream().allMatch(s ->
                s.getFirstName().toLowerCase().contains(lowerCaseKeyword) ||
                        s.getLastName().toLowerCase().contains(lowerCaseKeyword) ||
                        s.getEmail().toLowerCase().contains(lowerCaseKeyword)
        ));
    }

    @Test
    void findByOrderDetailsId() {
        when(clientRepository.findByOrderDetailsId("1")).thenReturn(Optional.of(clientEntity));

        Optional<ClientEntity> existentClient = clientRepository.findByOrderDetailsId("1");

        assertNotNull(existentClient);
        existentClient.ifPresent(entity -> {
            assertTrue(entity.getPendingOrders().stream()
                    .anyMatch(order -> "1".equals(order.getId())));
        });
    }
}
