package com.caterpillar.platform.mining.interfaces.rest.transform;

import com.caterpillar.platform.mining.domain.model.aggregates.MiningEquipmentUnit;
import com.caterpillar.platform.mining.interfaces.rest.resources.MiningEquipmentUnitResource;

/**
 * Converts a mining equipment unit aggregate into its API response resource.
 */
public final class MiningEquipmentUnitResourceFromEntityAssembler {

    private MiningEquipmentUnitResourceFromEntityAssembler() {
    }

    public static MiningEquipmentUnitResource toResourceFromEntity(MiningEquipmentUnit equipmentUnit) {
        return new MiningEquipmentUnitResource(
                equipmentUnit.getId(),
                equipmentUnit.getEquipmentUnitId().getValue(),
                equipmentUnit.getModel(),
                equipmentUnit.getSerialNumber(),
                equipmentUnit.getOperationStatus(),
                equipmentUnit.getAssignedMineSite(),
                equipmentUnit.getGpsLocation().getLatitude(),
                equipmentUnit.getGpsLocation().getLongitude(),
                equipmentUnit.getHoursOfOperation(),
                equipmentUnit.getCreatedAt(),
                equipmentUnit.getUpdatedAt());
    }
}
