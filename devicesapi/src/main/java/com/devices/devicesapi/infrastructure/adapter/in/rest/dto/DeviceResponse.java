package com.devices.devicesapi.infrastructure.adapter.in.rest.dto;

import com.devices.devicesapi.domain.model.DeviceState;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class DeviceResponse {
    private UUID id;
    private String name;
    private String brand;
    private DeviceState state;
    private LocalDateTime creationTime;
}
