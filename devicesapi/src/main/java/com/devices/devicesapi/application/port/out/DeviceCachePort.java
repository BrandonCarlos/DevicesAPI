package com.devices.devicesapi.application.port.out;

import com.devices.devicesapi.domain.model.Device;

import java.util.Optional;
import java.util.UUID;

public interface DeviceCachePort {
    Optional<Device> get(UUID id);
    void put(Device device);
    void evict(UUID id);
}
