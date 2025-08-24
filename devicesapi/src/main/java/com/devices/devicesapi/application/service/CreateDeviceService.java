package com.devices.devicesapi.application.service;

import com.devices.devicesapi.application.dto.DeviceRequest;
import com.devices.devicesapi.application.dto.DeviceResponse;
import com.devices.devicesapi.application.port.in.CreateDeviceUseCase;
import com.devices.devicesapi.application.port.out.DeviceRepositoryPort;
import com.devices.devicesapi.domain.model.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.devices.devicesapi.application.service.DeviceMapper.toResponse;

@Service
@RequiredArgsConstructor
public class CreateDeviceService implements CreateDeviceUseCase {

    private final DeviceRepositoryPort repository;

    @Override
    public DeviceResponse create(DeviceRequest request) {
        Device device = new Device(request.name(), request.brand(), request.state());
        return toResponse(repository.save(device));
    }
}