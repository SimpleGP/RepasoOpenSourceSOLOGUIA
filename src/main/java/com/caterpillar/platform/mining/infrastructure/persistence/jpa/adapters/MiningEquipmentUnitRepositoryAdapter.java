package com.caterpillar.platform.mining.infrastructure.persistence.jpa.adapters;

import com.caterpillar.platform.mining.domain.model.aggregates.MiningEquipmentUnit;
import com.caterpillar.platform.mining.domain.repositories.MiningEquipmentUnitRepository;
import com.caterpillar.platform.mining.infrastructure.persistence.jpa.repositories.MiningEquipmentUnitJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Adapts the domain repository contract to Spring Data JPA.
 */
@Repository
public class MiningEquipmentUnitRepositoryAdapter implements MiningEquipmentUnitRepository {

    private final MiningEquipmentUnitJpaRepository jpaRepository;

    public MiningEquipmentUnitRepositoryAdapter(MiningEquipmentUnitJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean existsByAssignedMineSiteAndSerialNumberFingerprint(String assignedMineSite, String serialNumberFingerprint) {
        return jpaRepository.existsByAssignedMineSiteAndSerialNumberFingerprint(assignedMineSite, serialNumberFingerprint);
    }

    @Override
    public MiningEquipmentUnit save(MiningEquipmentUnit equipmentUnit) {
        return jpaRepository.save(equipmentUnit);
    }
}
