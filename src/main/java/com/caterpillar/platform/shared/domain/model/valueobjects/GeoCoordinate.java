package com.caterpillar.platform.shared.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Represents a geographic coordinate with valid latitude and longitude values.
 */
@Embeddable
public class GeoCoordinate {

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    protected GeoCoordinate() {
    }

    public GeoCoordinate(double latitude, double longitude) {
        if (!Double.isFinite(latitude) || latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90.");
        }
        if (!Double.isFinite(longitude) || longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180.");
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
