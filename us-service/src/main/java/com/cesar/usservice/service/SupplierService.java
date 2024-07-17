package com.cesar.usservice.service;

import com.cesar.usservice.dto.SupplierDTO;

public interface SupplierService {
    SupplierDTO getSupplierById(String id);
    SupplierDTO saveSupplier(SupplierDTO supplierDTO);
    SupplierDTO updateSupplierById(SupplierDTO supplierDTO, String id);
    String deleteSupplierById(String id);
}
