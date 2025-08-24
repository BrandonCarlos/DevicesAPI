package com.techlogy.devices.application.service;

import com.techlogy.devices.application.dto.DeviceResponse;
import com.techlogy.devices.application.port.in.UpdateDeviceUseCase;
import com.techlogy.devices.application.port.out.DeviceRepositoryPort;
import com.techlogy.devices.domain.exception.InvalidOperationException;
import com.techlogy.devices.domain.exception.NotFoundException;
import com.techlogy.devices.domain.model.Device;
import com.techlogy.devices.domain.model.DeviceState;
import com.techlogy.devices.infrastructure.adapter.in.rest.dto.DeviceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.techlogy.devices.application.service.DeviceMapper.toResponse;

@Service
@RequiredArgsConstructor
public class UpdateDeviceService implements UpdateDeviceUseCase {

    private final DeviceRepositoryPort repository;

    @Override
    public DeviceResponse update(UUID id, DeviceRequest request) {
        Device existingDevice = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));

        // Create a new Device object with updated fields but keep id and creationTime from existing device
        Device updatedDevice = new Device(
                id,
                request.getName(),
                request.getBrand(),
                request.getState(),
                existingDevice.getCreationTime()
        );

        // Delegate the update and validation logic to the Device entity
        existingDevice.update(updatedDevice);

        // Save the updated device and convert to response DTO
        return toResponse(repository.save(existingDevice));
    }

    @Override
    public DeviceResponse patch(UUID id, Map<String, Object> updates) {
        Device device = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));

        // Extract current values
        String name = device.getName();
        String brand = device.getBrand();
        DeviceState state = device.getState();
        LocalDateTime creationTime = device.getCreationTime();

        // Override with updates if present
        if (updates.containsKey("name")) {
            name = (String) updates.get("name");
        }
        if (updates.containsKey("brand")) {
            brand = (String) updates.get("brand");
        }
        if (updates.containsKey("state")) {
            state = DeviceState.valueOf((String) updates.get("state"));
        }
        if (updates.containsKey("creationTime")) {
            throw new InvalidOperationException("Cannot update creation time");
        }

        // Build new Device with updated data
        Device updatedDevice = new Device(device.getId(), name, brand, state, creationTime);

        // Delegate validation & update to the domain model
        device.update(updatedDevice);

        // Save and return
        return toResponse(repository.save(device));
    }
}
