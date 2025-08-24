package com.devices.devicesapi.domain.model;

import com.devices.devicesapi.domain.exception.InvalidOperationException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class Device {
    private final UUID id;
    private String name;
    private String brand;
    private DeviceState state;
    private final LocalDateTime creationTime;

    public Device(String name, String brand, DeviceState state) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.brand = brand;
        this.state = state;
        this.creationTime = LocalDateTime.now();
    }

    public Device(UUID id, String name, String brand, DeviceState state, LocalDateTime creationTime) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.state = state;
        this.creationTime = creationTime;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public DeviceState getState() { return state; }
    public LocalDateTime getCreationTime() { return creationTime; }

    public void update(Device updatedDevice) {
        if (this.state == DeviceState.IN_USE &&
                (!this.name.equals(updatedDevice.name) || !this.brand.equals(updatedDevice.brand))) {
            throw new InvalidOperationException("Cannot update name or brand of a device that is in use");
        }

        if (!this.creationTime.equals(updatedDevice.creationTime)) {
            throw new InvalidOperationException("Creation time cannot be updated");
        }

        // Apply updates
        this.name = updatedDevice.name;
        this.brand = updatedDevice.brand;
        this.state = updatedDevice.state;
    }

    // Partial update (patch)
    public void patch(Map<String, Object> fields) {
        if (state == DeviceState.IN_USE &&
                (fields.containsKey("name") || fields.containsKey("brand"))) {
            throw new InvalidOperationException("Cannot update name/brand of device in use");
        }
        for (var entry : fields.entrySet()) {
            switch (entry.getKey()) {
                case "name" -> this.name = (String) entry.getValue();
                case "brand" -> this.brand = (String) entry.getValue();
                case "state" -> this.state = DeviceState.valueOf((String) entry.getValue());
                case "creationTime" -> throw new InvalidOperationException("Cannot update creation time");
                default -> throw new InvalidOperationException("Invalid field: " + entry.getKey());
            }
        }
    }

    // Domain validation for deletion
    public void validateDeletion() {
        if (this.state == DeviceState.IN_USE) {
            throw new InvalidOperationException("In-use devices cannot be deleted");
        }
    }
}
