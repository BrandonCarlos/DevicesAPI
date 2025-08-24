package com.devices.devicesapi.application.port.in;

import java.util.UUID;

public interface DeleteDeviceUseCase {
    void delete(UUID id);
}
