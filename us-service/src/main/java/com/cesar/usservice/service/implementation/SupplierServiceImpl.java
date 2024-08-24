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

/**
 * Implementation of the SupplierService interface, handling business logic for supplier-related operations.
 */
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

    /**
     * Initializes the map that defines how to update each field of a supplier.
     */
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
        updateFieldMap.put(SupplierField.MAX_QUOTA, this::maxQuota);
        updateFieldMap.put(SupplierField.MIN_QUOTA, this::minQuota);
    }

    private void maxQuota(SupplierEntity entity, SupplierDTO request) {
        if (request.getMaxQuota() == 0) {
            entity.setMaxQuota(999999999);
        }
        if (request.getMaxQuota() > 0 && request.getMaxQuota() > entity.getMinQuota() && request.getMaxQuota() != entity.getMaxQuota()) {
            entity.setMaxQuota(request.getMaxQuota());
        }
    }

    private void minQuota(SupplierEntity entity, SupplierDTO request) {
        if (request.getMinQuota() == 0) {
            entity.setMinQuota(999999999);
        }
        if (request.getMinQuota() > 0 && request.getMinQuota() < entity.getMinQuota()) {
            entity.setMinQuota(request.getMinQuota());
        }
    }


    /**
     * Retrieves a supplier by its ID.
     *
     * @param id the ID of the supplier
     * @return the SupplierDTO
     * @throws SupplierException if the supplier is not found
     */
    @Override
    public SupplierDTO getSupplierById(String id) {
        return supplierMapper.toDTO(supplierRepository.findSupplierEntityById(id)
                .orElseThrow(() -> new SupplierException("User not found with id: " + id)));
    }

    /**
     * Saves a new supplier.
     *
     * @param supplierDTO the supplier data to be saved
     * @return the saved SupplierDTO
     * @throws SupplierException if the supplier already exists or required fields are missing
     */
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

    /**
     * Updates an existing supplier by its ID.
     *
     * @param supplierDTO the new data for the supplier
     * @param id          the ID of the supplier to update
     * @return the updated SupplierDTO
     * @throws ClientException   if the supplier data is null
     * @throws SupplierException if the supplier is not found
     */
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

    /**
     * Deletes a supplier by its ID.
     *
     * @param id the ID of the supplier to delete
     * @return a confirmation message
     * @throws SupplierException if the supplier is not found
     */
    @Override
    public String deleteSupplierById(String id) {
        SupplierEntity supplierEntity = supplierMapper.toEntity(getSupplierById(id));
        if (supplierEntity != null) {
            supplierRepository.delete(supplierEntity);
            return "User with id: " + id + " successfully deleted";
        }
        logger.info("User not found with id {}", id);
        throw new SupplierException("User not found with id: " + id);
    }
}
