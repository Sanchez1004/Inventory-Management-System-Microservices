package com.cesar.usservice.service.implementation;

import com.cesar.usservice.dto.SupplierDTO;
import com.cesar.usservice.dto.mapper.SupplierMapper;
import com.cesar.usservice.entity.SupplierEntity;
import com.cesar.usservice.exception.ClientException;
import com.cesar.usservice.exception.SupplierException;
import com.cesar.usservice.repository.SupplierRepository;
import com.cesar.usservice.service.SupplierService;
import com.cesar.usservice.utils.SupplierField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class SupplierServiceImpl implements SupplierService {

    private static final Logger logger = LogManager.getLogger(SupplierServiceImpl.class);

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final Map<SupplierField, BiConsumer<SupplierEntity, SupplierDTO>> updateFieldMap = new EnumMap<>(SupplierField.class);

    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;

        initializeUpdateFieldMap();
    }

    private void initializeUpdateFieldMap() {
        updateFieldMap.put(SupplierField.ID, (entity, request) -> {
            if (request != null) {
                entity.setId(request.getId());
            }
        });
        updateFieldMap.put(SupplierField.NAME, (entity, request) -> {
            if (request != null) {
                entity.setName(request.getName());
            }
        });
        updateFieldMap.put(SupplierField.EMAIL, (entity, request) -> {
            if (request.getEmail() != null) {
                entity.setEmail(request.getEmail());
            }
        });
        updateFieldMap.put(SupplierField.ADDRESS, (entity, request) -> {
            if (request.getAddress() != null) {
                entity.setAddress(request.getAddress());
            }
        });
        updateFieldMap.put(SupplierField.MAX_QUOTA, (entity, request) -> {
            if (request.getAddress() != null) {
                entity.setMaxQuota(request.getMaxQuota());
            }
        });
        updateFieldMap.put(SupplierField.MIN_QUOTA, (entity, request) -> {
            if (request.getAddress() != null) {
                entity.setMinQuota(request.getMinQuota());
            }
        });
    }

    @Override
    public SupplierDTO getSupplierById(String id) {
        return supplierMapper.toDTO(supplierRepository.findSupplierEntityById(id)
                .orElseThrow(() -> new SupplierException("User not found with id: " + id)));
    }

    @Override
    public SupplierDTO saveSupplier(SupplierDTO supplierDTO) {
        if (supplierDTO.getId() != null && supplierDTO.getName() != null && supplierDTO.getEmail() != null) {
            if (supplierRepository.findSupplierEntityByIdAndNameAndEmail(supplierDTO.getId(), supplierDTO.getName(), supplierDTO.getEmail()) != null) {
                logger.info("User already exists with id {}", supplierDTO.getId());
                throw new SupplierException("User already exists");
            }
            return supplierMapper.toDTO(supplierRepository.save(supplierMapper.toEntity(supplierDTO)));
        }
        throw new SupplierException("User id, email and name cannot be empty");
    }

    @Override
    public SupplierDTO updateSupplierById(SupplierDTO supplierDTO, String id) {
        if (supplierDTO == null) {
            logger.error("At least one field of the new supplier has to be not null");
            throw new ClientException("Supplier request cannot be null");
        }

        SupplierEntity supplierEntity = supplierMapper.toEntity(getSupplierById(id));

        for (Map.Entry<SupplierField, BiConsumer<SupplierEntity, SupplierDTO>> entry : updateFieldMap.entrySet()) {
            entry.getValue().accept(supplierEntity, supplierDTO);
        }

        return supplierMapper.toDTO(supplierRepository.save(supplierEntity));
    }

    @Override
    public String deleteSupplierById(String id) {
        SupplierEntity supplierEntity = supplierMapper.toEntity(getSupplierById(id));
        if (supplierEntity != null) {
            supplierRepository.delete(supplierEntity);
            return "User with id: " + id + "successfully deleted";
        }
        logger.info("User not found with id {}", id);
        throw new SupplierException("User not found with id: " + id);
    }
}
