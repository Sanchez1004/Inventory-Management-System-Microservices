package com.cesar.usservice.service;

import com.cesar.usservice.dto.ClientDTO;
import com.cesar.usservice.dto.OrderDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientService {
    ClientDTO getClientById(String id);
    List<ClientDTO> getListOfClientsByKeyword(String keyword);
    ClientDTO getClientByOrderId(String orderId);
    ClientDTO saveClient(ClientDTO clientDTO);
    ClientDTO updateClientById(ClientDTO clientDTO, String id);
    ClientDTO updateClientOrdersByClientId(OrderDetails orderDetails, String id);
    ClientDTO updateClientOrderStatusByClientId(String orderId, String newOrderStatus, String clientId);
    String deleteClientById(String id);
}
