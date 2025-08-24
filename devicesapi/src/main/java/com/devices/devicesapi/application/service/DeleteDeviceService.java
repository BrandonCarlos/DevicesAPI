package com.devices.devicesapi.application.service;

import com.devices.devicesapi.application.port.in.DeleteDeviceUseCase;
import com.devices.devicesapi.application.port.out.DeviceCachePort;
import com.devices.devicesapi.application.port.out.DeviceRepositoryPort;
import com.devices.devicesapi.domain.exception.InvalidOperationException;
import com.devices.devicesapi.domain.model.Device;
import com.devices.devicesapi.domain.model.DeviceState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteDeviceService implements DeleteDeviceUseCase {

    private final DeviceRepositoryPort repository;
    private final DeviceCachePort cache;

    @Override
    public void delete(UUID id) {
        Device device = repository.findById(id)
                .orElseThrow(() -> new InvalidOperationException("Device not found"));

        if (device.getState() == DeviceState.IN_USE) {
            throw new InvalidOperationException("Cannot delete a device that is in use");
        }

        repository.deleteById(id);
        cache.evict(id);
    }
}
