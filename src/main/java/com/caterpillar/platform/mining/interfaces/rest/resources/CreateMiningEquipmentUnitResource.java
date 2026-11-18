package com.caterpillar.platform.mining.interfaces.rest.resources;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request body used to register a mining equipment unit.
 *
 * @param model equipment model name
 * @param serialNumber equipment serial number
 * @param operationStatus allowed operational status
 * @param assignedMineSite assigned mine site
 * @param gpsLatitude latitude from -90 through 90
 * @param gpsLongitude longitude from -180 through 180
 * @param hoursOfOperation non-negative accumulated operating hours
 */
public record CreateMiningEquipmentUnitResource(
        @NotBlank @Size(max = 255) String model,
        @NotBlank @Size(max = 256) String serialNumber,
        @NotBlank @Pattern(regexp = "ACTIVE|IN_MAINTENANCE|INACTIVE") String operationStatus,
        @NotBlank @Size(max = 255) String assignedMineSite,
        @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") Double gpsLatitude,
        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") Double gpsLongitude,
        @NotNull @Min(0) Integer hoursOfOperation) {
}
