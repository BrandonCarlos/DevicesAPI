package com.techlogy.devices.application.service;

import com.techlogy.devices.application.dto.DeviceResponse;
import com.techlogy.devices.domain.model.Device;

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