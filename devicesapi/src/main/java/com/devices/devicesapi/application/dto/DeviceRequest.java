package com.devices.devicesapi.application.dto;

import com.devices.devicesapi.domain.model.DeviceState;

public record DeviceRequest(
        String name,
        String brand,
        DeviceState state
) {}
