package com.caterpillar.platform.mining.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Wraps an allowed operational status for a mining equipment unit.
 */
@Embeddable
public class OperationStatus {

    public enum Value {
        ACTIVE,
        IN_MAINTENANCE,
        INACTIVE
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private Value value;

    protected OperationStatus() {
    }

    public OperationStatus(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Operation status cannot be null or blank.");
        }
        try {
            this.value = Value.valueOf(value);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Operation status must be ACTIVE, IN_MAINTENANCE, or INACTIVE.");
        }
    }

    public Value getValue() {
        return value;
    }
}
