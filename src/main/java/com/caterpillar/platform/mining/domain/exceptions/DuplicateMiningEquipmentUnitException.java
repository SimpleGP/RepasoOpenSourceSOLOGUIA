package com.caterpillar.platform.mining.domain.exceptions;

/**
 * Indicates that a serial number is already registered at the assigned mine site.
 */
public class DuplicateMiningEquipmentUnitException extends RuntimeException {

    public DuplicateMiningEquipmentUnitException() {
        super("A mining equipment unit with this serial number already exists at the assigned mine site.");
    }
}
