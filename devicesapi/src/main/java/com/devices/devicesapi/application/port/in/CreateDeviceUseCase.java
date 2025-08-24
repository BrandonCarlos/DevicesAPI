package com.devices.devicesapi.application.port.in;

import com.devices.devicesapi.application.dto.DeviceRequest;
import com.devices.devicesapi.application.dto.DeviceResponse;

public interface CreateDeviceUseCase {
    DeviceResponse create(DeviceRequest request);
}
