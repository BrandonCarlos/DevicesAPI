package com.devices.devicesapi.application.dto;

import com.devices.devicesapi.domain.model.DeviceState;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeviceResponse(
        UUID id,
        String name,
        String brand,
        DeviceState state,
        LocalDateTime creationTime
) {}
