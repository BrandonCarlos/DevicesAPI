package com.techlogy.devices.application.service;

import com.techlogy.devices.application.dto.DeviceResponse;
import com.techlogy.devices.application.port.in.GetDeviceUseCase;
import com.techlogy.devices.application.port.out.DeviceCachePort;
import com.techlogy.devices.application.port.out.DeviceRepositoryPort;
import com.techlogy.devices.domain.exception.NotFoundException;
import com.techlogy.devices.domain.model.Device;
import com.techlogy.devices.domain.model.DeviceState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetDeviceService implements GetDeviceUseCase {

    private final DeviceRepositoryPort repository;
    private final DeviceCachePort cache;

    public DeviceResponse getById(UUID id) {
        // Check cache first
        Optional<Device> cached = cache.get(id);
        if (cached.isPresent()) {
            return toResponse(cached.get());
        }

        // Fallback to DB
        Device device = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));

        // Cache it
        cache.put(device);

        return toResponse(device);
    }

    private DeviceResponse toResponse(Device device) {
        return new DeviceResponse(
                device.getId(),
                device.getName(),
                device.getBrand(),
                device.getState(),
                device.getCreationTime()
        );
    }

    @Override
    public List<DeviceResponse> getAll() {
        return repository.findAll().stream().map(DeviceMapper::toResponse).toList();
    }

    public List<DeviceResponse> getByState(DeviceState state) {
        return devices.stream().map(this::toResponse).toList();
    }

    @Override
    public List<DeviceResponse> getByBrand(String brand) {
        return repository.findByBrand(brand).stream().map(DeviceMapper::toResponse).toList();
    }
}
