package com.caterpillar.platform.mining.infrastructure.persistence.jpa.repositories;

import com.caterpillar.platform.mining.domain.model.aggregates.MiningEquipmentUnit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for mining equipment unit aggregates.
 */
public interface MiningEquipmentUnitJpaRepository extends JpaRepository<MiningEquipmentUnit, Integer> {

    boolean existsByAssignedMineSiteAndSerialNumberFingerprint(String assignedMineSite, String serialNumberFingerprint);
}
