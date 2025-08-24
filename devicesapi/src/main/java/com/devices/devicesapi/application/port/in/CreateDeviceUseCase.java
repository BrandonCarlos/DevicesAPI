package com.techlogy.devices.application.port.in;

import com.techlogy.devices.application.dto.DeviceRequest;
import com.techlogy.devices.application.dto.DeviceResponse;

public interface CreateDeviceUseCase {
    DeviceResponse create(DeviceRequest request);
}
