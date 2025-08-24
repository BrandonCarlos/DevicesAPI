package com.devices.devicesapi.application.port.in;

import com.devices.devicesapi.application.dto.DeviceResponse;
import com.devices.devicesapi.domain.model.DeviceState;

import java.util.List;
import java.util.UUID;

public interface GetDeviceUseCase {
    DeviceResponse getById(UUID id);
    List<DeviceResponse> getAll();
//    List<DeviceResponse> getByState(DeviceState state);
    List<DeviceResponse> getByBrand(String brand);
}
