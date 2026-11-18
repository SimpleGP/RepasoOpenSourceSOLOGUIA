package com.caterpillar.platform.mining.domain.model.commands;

/**
 * Carries the input required to register a mining equipment unit.
 *
 * @param model equipment model name
 * @param serialNumber equipment serial number
 * @param operationStatus allowed operational status
 * @param assignedMineSite mine site assigned to the equipment
 * @param gpsLatitude geographic latitude
 * @param gpsLongitude geographic longitude
 * @param hoursOfOperation accumulated operating hours
 */
public record CreateMiningEquipmentUnitCommand(
        String model,
        String serialNumber,
        String operationStatus,
        String assignedMineSite,
        double gpsLatitude,
        double gpsLongitude,
        int hoursOfOperation) {
}
