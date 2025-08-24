package com.techlogy.devices.application.port.in;

import com.techlogy.devices.application.dto.DeviceResponse;
import com.techlogy.devices.infrastructure.adapter.in.rest.dto.DeviceRequest;

import java.util.Map;
import java.util.UUID;

public interface UpdateDeviceUseCase {
    DeviceResponse update(UUID id, DeviceRequest request);  // full update
    DeviceResponse patch(UUID id, Map<String, Object> fields);
}
