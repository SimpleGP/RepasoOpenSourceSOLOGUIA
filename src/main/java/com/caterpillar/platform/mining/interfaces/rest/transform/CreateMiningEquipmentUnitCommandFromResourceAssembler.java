package com.caterpillar.platform.mining.interfaces.rest.transform;

import com.caterpillar.platform.mining.domain.model.commands.CreateMiningEquipmentUnitCommand;
import com.caterpillar.platform.mining.interfaces.rest.resources.CreateMiningEquipmentUnitResource;

/**
 * Converts an HTTP request resource into a domain command.
 */
public final class CreateMiningEquipmentUnitCommandFromResourceAssembler {

    private CreateMiningEquipmentUnitCommandFromResourceAssembler() {
    }

    public static CreateMiningEquipmentUnitCommand toCommandFromResource(CreateMiningEquipmentUnitResource resource) {
        return new CreateMiningEquipmentUnitCommand(
                resource.model(),
                resource.serialNumber(),
                resource.operationStatus(),
                resource.assignedMineSite(),
                resource.gpsLatitude(),
                resource.gpsLongitude(),
                resource.hoursOfOperation());
    }
}
