package com.caterpillar.platform.mining.domain.model.aggregates;

import com.caterpillar.platform.mining.domain.model.commands.CreateMiningEquipmentUnitCommand;
import com.caterpillar.platform.mining.domain.model.valueobjects.CatMiningEquipmentUnitId;
import com.caterpillar.platform.mining.domain.model.valueobjects.OperationStatus;
import com.caterpillar.platform.mining.infrastructure.persistence.jpa.converters.SerialNumberAttributeConverter;
import com.caterpillar.platform.shared.domain.model.valueobjects.GeoCoordinate;
import com.caterpillar.platform.shared.infrastructure.persistence.jpa.AuditableAbstractAggregateRoot;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.nio.charset.StandardCharsets;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.GeneralSecurityException;
import java.util.HexFormat;

/**
 * Aggregate root representing a registered mining equipment unit.
 */
@Entity
@Table(name = "mining_equipment_units", uniqueConstraints = {
        @UniqueConstraint(name = "uk_equipment_site_serial_fingerprint",
                columnNames = {"assigned_mine_site", "serial_number_fingerprint"})
})
public class MiningEquipmentUnit extends AuditableAbstractAggregateRoot<MiningEquipmentUnit> {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "equipment_unit_id", nullable = false, length = 36, unique = true))
    private CatMiningEquipmentUnitId equipmentUnitId;

    @Column(nullable = false)
    private String model;

    @Convert(converter = SerialNumberAttributeConverter.class)
    @Column(nullable = false, name = "serial_number", length = 512)
    private String serialNumber;

    @Column(name = "serial_number_fingerprint", nullable = false, length = 64)
    private String serialNumberFingerprint;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "operation_status", nullable = false, length = 24))
    private OperationStatus operationStatus;

    @Column(name = "assigned_mine_site", nullable = false)
    private String assignedMineSite;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "gps_latitude", nullable = false)),
            @AttributeOverride(name = "longitude", column = @Column(name = "gps_longitude", nullable = false))
    })
    private GeoCoordinate gpsLocation;

    @Column(name = "hours_of_operation", nullable = false)
    private int hoursOfOperation;

    protected MiningEquipmentUnit() {
    }

    public MiningEquipmentUnit(CreateMiningEquipmentUnitCommand command) {
        if (command.model() == null || command.model().isBlank()) {
            throw new IllegalArgumentException("Model cannot be blank.");
        }
        if (command.serialNumber() == null || command.serialNumber().isBlank()) {
            throw new IllegalArgumentException("Serial number cannot be blank.");
        }
        if (command.assignedMineSite() == null || command.assignedMineSite().isBlank()) {
            throw new IllegalArgumentException("Assigned mine site cannot be blank.");
        }
        if (command.hoursOfOperation() < 0) {
            throw new IllegalArgumentException("Hours of operation cannot be negative.");
        }

        this.equipmentUnitId = CatMiningEquipmentUnitId.generate();
        this.model = command.model().trim();
        this.serialNumber = command.serialNumber().trim();
        this.serialNumberFingerprint = fingerprint(this.serialNumber);
        this.operationStatus = new OperationStatus(command.operationStatus());
        this.assignedMineSite = command.assignedMineSite().trim();
        this.gpsLocation = new GeoCoordinate(command.gpsLatitude(), command.gpsLongitude());
        this.hoursOfOperation = command.hoursOfOperation();
    }

    /**
     * Creates the keyed fingerprint used to enforce uniqueness with the assigned mine site.
     *
     * @param serialNumber serial number to fingerprint
     * @return lowercase hexadecimal HMAC-SHA-256 fingerprint
     */
    private static String fingerprint(String serialNumber) {
        try {
            String secret = System.getProperty("serial.encryption.secret");
            if (secret == null || secret.isBlank()) {
                secret = System.getenv("SERIAL_ENCRYPTION_SECRET");
            }
            if (secret == null || secret.isBlank()) {
                secret = "local-development-secret-change-before-deployment";
            }
            byte[] key = MessageDigest.getInstance("SHA-256").digest(secret.getBytes(StandardCharsets.UTF_8));
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            byte[] digest = mac.doFinal(serialNumber.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Serial number fingerprinting failed.", exception);
        }
    }

    public CatMiningEquipmentUnitId getEquipmentUnitId() {
        return equipmentUnitId;
    }

    public String getModel() {
        return model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getOperationStatus() {
        return operationStatus.getValue().name();
    }

    public String getAssignedMineSite() {
        return assignedMineSite;
    }

    public GeoCoordinate getGpsLocation() {
        return gpsLocation;
    }

    public int getHoursOfOperation() {
        return hoursOfOperation;
    }

    public String getSerialNumberFingerprint() {
        return serialNumberFingerprint;
    }
}
