package com.devices.devicesapi.application.port.in;

import com.devices.devicesapi.application.dto.DeviceRequest;
import com.devices.devicesapi.application.dto.DeviceResponse;
import com.devices.devicesapi.domain.model.DeviceState;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface DeviceUseCase {
    DeviceResponse create(DeviceRequest request);
    DeviceResponse update(UUID id, DeviceRequest request);
    DeviceResponse patch(UUID id, Map<String, Object> updates);
    DeviceResponse getById(UUID id);
    List<DeviceResponse> getAll();
    List<DeviceResponse> getByBrand(String brand);
    List<DeviceResponse> getByState(DeviceState state);
    void delete(UUID id);
}
