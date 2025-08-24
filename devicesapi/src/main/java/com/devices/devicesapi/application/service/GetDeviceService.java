package com.devices.devicesapi.application.service;

import com.devices.devicesapi.application.dto.DeviceResponse;
import com.devices.devicesapi.application.port.in.GetDeviceUseCase;
import com.devices.devicesapi.application.port.out.DeviceCachePort;
import com.devices.devicesapi.application.port.out.DeviceRepositoryPort;
import com.devices.devicesapi.domain.exception.NotFoundException;
import com.devices.devicesapi.domain.model.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.devices.devicesapi.application.service.DeviceMapper.toResponse;

@Service
@RequiredArgsConstructor
public class GetDeviceService implements GetDeviceUseCase {

    private final DeviceRepositoryPort repository;
    private final DeviceCachePort cache;

    @Override
    public DeviceResponse getById(UUID id) {
        Optional<Device> cached = cache.get(id);
        if (cached.isPresent()) {
            return toResponse(cached.get());
        }

        Device device = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));

        cache.put(device);

        // 4. Return the response
        return toResponse(device);
    }

    @Override
    public List<DeviceResponse> getAll() {
        return repository.findAll().stream()
                .map(DeviceMapper::toResponse)
                .toList();
    }

    @Override
    public List<DeviceResponse> getByBrand(String brand) {
        return repository.findByBrand(brand).stream()
                .map(DeviceMapper::toResponse)
                .toList();
    }
}