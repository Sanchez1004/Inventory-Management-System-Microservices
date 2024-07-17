package com.cesar.usservice.service;

import com.cesar.usservice.dto.ClientDTO;
import com.cesar.usservice.dto.OrderDetails;

import java.util.List;

public interface ClientService {
    ClientDTO getClientById(String id);
    List<ClientDTO> getListOfClientsByKeyword(String keyword);
    ClientDTO getClientByOrderId(String id);
    ClientDTO saveClient(ClientDTO clientDTO);
    ClientDTO updateClient(ClientDTO clientDTO, String id);
    ClientDTO updateClientOrdersByClientId(OrderDetails orderDetails, String id);
    ClientDTO updateClientOrderStatusByClientId(String orderId, String newOrderStatus, String clientId);
    String deleteClientById(String id);
}
