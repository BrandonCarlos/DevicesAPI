package com.techlogy.devices.application.port.out;

import com.techlogy.devices.application.dto.DeviceResponse;
import com.techlogy.devices.domain.model.Device;

import java.util.Optional;
import java.util.UUID;

public interface DeviceCachePort {
    Optional<Device> get(UUID id);
    void put(Device device);
    void evict(UUID id);
}
