package com.devices.devicesapi.infrastructure.adapter.in.rest.dto;

import com.devices.devicesapi.domain.model.DeviceState;
import lombok.Data;

@Data
public class DeviceRequest {
    private String name;
    private String brand;
    private DeviceState state;
}
