package com.devices.devicesapi.application.service;

import com.devices.devicesapi.application.dto.DeviceRequest;
import com.devices.devicesapi.application.dto.DeviceResponse;
import com.devices.devicesapi.application.port.in.DeviceUseCase;
import com.devices.devicesapi.application.port.out.DeviceRepositoryPort;
import com.devices.devicesapi.domain.exception.NotFoundException;
import com.devices.devicesapi.domain.model.Device;
import com.devices.devicesapi.domain.model.DeviceState;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceService implements DeviceUseCase {

    @Autowired
    private DeviceRepositoryPort repository;

    private static DeviceResponse toResponse(Device device) {
        return new DeviceResponse(
                device.getId(),
                device.getName(),
                device.getBrand(),

                device.getState(),
                device.getCreationTime()
        );
    }

    @Override
    public DeviceResponse create(DeviceRequest request) {
        Device device = new Device(request.name(), request.brand(), request.state());
        return toResponse(repository.save(device));
    }

    @Override
    public DeviceResponse update(UUID id, DeviceRequest request) {
        Device existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));
        Device updated = new Device(id, request.name(), request.brand(), request.state(), existing.getCreationTime());
        existing.update(updated);  // Domain method handles validation and update
        return toResponse(repository.save(existing));
    }

    @Override
    public DeviceResponse patch(UUID id, Map<String, Object> updates) {
        Device existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));
        existing.patch(updates);   // Domain method to partially update and validate
        return toResponse(repository.save(existing));
    }

    @Override
    public DeviceResponse getById(UUID id) {
        Device device = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));
        return toResponse(device);
    }

    @Override
    public List<DeviceResponse> getAll() {
        return repository.findAll().stream()
                .map(DeviceService::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceResponse> getByBrand(String brand) {
        return repository.findByBrand(brand).stream()
                .map(DeviceService::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceResponse> getByState(DeviceState state) {
        return repository.findByState(state).stream()
                .map(DeviceService::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        Device existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));
        existing.validateDeletion();  // Throws exception if not allowed
        repository.deleteById(existing.getId());
    }
}
