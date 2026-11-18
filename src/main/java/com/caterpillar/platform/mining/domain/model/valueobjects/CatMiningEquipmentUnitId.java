package com.caterpillar.platform.mining.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.UUID;

/**
 * Represents the business identifier generated for a mining equipment unit.
 */
@Embeddable
public class CatMiningEquipmentUnitId {

    @Column(name = "equipment_unit_id", nullable = false, unique = true, length = 36)
    private String value;

    protected CatMiningEquipmentUnitId() {
    }

    public CatMiningEquipmentUnitId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Equipment unit identifier cannot be null or blank.");
        }
        this.value = UUID.fromString(value).toString();
    }

    public static CatMiningEquipmentUnitId generate() {
        return new CatMiningEquipmentUnitId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }
}
