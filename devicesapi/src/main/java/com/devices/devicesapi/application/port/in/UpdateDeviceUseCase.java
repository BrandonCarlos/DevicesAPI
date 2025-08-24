package com.devices.devicesapi.application.port.in;


import com.devices.devicesapi.application.dto.DeviceResponse;
import com.devices.devicesapi.infrastructure.adapter.in.rest.dto.DeviceRequest;

import java.util.Map;
import java.util.UUID;

public interface UpdateDeviceUseCase {
    DeviceResponse update(UUID id, DeviceRequest request);  // full update
    DeviceResponse patch(UUID id, Map<String, Object> fields);
}
