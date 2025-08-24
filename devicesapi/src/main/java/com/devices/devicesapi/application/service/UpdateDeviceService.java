package com.devices.devicesapi.application.service;

import com.devices.devicesapi.application.dto.DeviceResponse;
import com.devices.devicesapi.application.port.in.UpdateDeviceUseCase;
import com.devices.devicesapi.application.port.out.DeviceRepositoryPort;
import com.devices.devicesapi.domain.exception.InvalidOperationException;
import com.devices.devicesapi.domain.exception.NotFoundException;
import com.devices.devicesapi.domain.model.Device;
import com.devices.devicesapi.domain.model.DeviceState;
import com.devices.devicesapi.infrastructure.adapter.in.rest.dto.DeviceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static com.devices.devicesapi.application.service.DeviceMapper.toResponse;


@Service
@RequiredArgsConstructor
public class UpdateDeviceService implements UpdateDeviceUseCase {

    private final DeviceRepositoryPort repository;

    @Override
    public DeviceResponse update(UUID id, DeviceRequest request) {
        Device existingDevice = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));

        Device updatedDevice = new Device(
                id,
                request.getName(),
                request.getBrand(),
                request.getState(),
                existingDevice.getCreationTime()
        );

        existingDevice.update(updatedDevice);

        return toResponse(repository.save(existingDevice));
    }

    @Override
    public DeviceResponse patch(UUID id, Map<String, Object> updates) {
        Device device = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));

        String name = device.getName();
        String brand = device.getBrand();
        DeviceState state = device.getState();
        LocalDateTime creationTime = device.getCreationTime();

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

        Device updatedDevice = new Device(device.getId(), name, brand, state, creationTime);

        device.update(updatedDevice);

        return toResponse(repository.save(device));
    }
}
