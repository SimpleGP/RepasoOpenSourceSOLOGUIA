package com.caterpillar.platform.mining.domain.repositories;

import com.caterpillar.platform.mining.domain.model.aggregates.MiningEquipmentUnit;

/**
 * Defines persistence operations required by the mining domain.
 */
public interface MiningEquipmentUnitRepository {

    boolean existsByAssignedMineSiteAndSerialNumberFingerprint(String assignedMineSite, String serialNumberFingerprint);

    MiningEquipmentUnit save(MiningEquipmentUnit equipmentUnit);
}
