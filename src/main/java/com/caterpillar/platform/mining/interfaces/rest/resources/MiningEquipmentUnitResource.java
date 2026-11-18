package com.caterpillar.platform.mining.interfaces.rest.resources;

import java.time.Instant;

/**
 * Response representation of a registered mining equipment unit.
 *
 * @param id generated database identifier
 * @param equipmentUnitId generated business identifier
 * @param model equipment model name
 * @param serialNumber decrypted equipment serial number
 * @param operationStatus operational status
 * @param assignedMineSite assigned mine site
 * @param gpsLatitude geographic latitude
 * @param gpsLongitude geographic longitude
 * @param hoursOfOperation accumulated operating hours
 * @param createdAt creation timestamp
 * @param updatedAt last update timestamp
 */
public record MiningEquipmentUnitResource(
        Integer id,
        String equipmentUnitId,
        String model,
        String serialNumber,
        String operationStatus,
        String assignedMineSite,
        double gpsLatitude,
        double gpsLongitude,
        int hoursOfOperation,
        Instant createdAt,
        Instant updatedAt) {
}
