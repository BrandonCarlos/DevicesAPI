package com.devices.devicesapi.application.service;

import com.devices.devicesapi.application.dto.DeviceResponse;
import com.devices.devicesapi.domain.model.Device;

public class DeviceMapper {

    public static DeviceResponse toResponse(Device device) {
        return new DeviceResponse(
                device.getId(),
                device.getName(),
                device.getBrand(),
                device.getState(),
                device.getCreationTime()
        );
    }
}