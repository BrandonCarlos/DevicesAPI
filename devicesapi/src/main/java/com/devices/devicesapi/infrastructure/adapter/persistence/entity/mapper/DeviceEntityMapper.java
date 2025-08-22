package com.devices.devicesapi.infrastructure.adapter.persistence.entity.mapper;

import com.devices.devicesapi.domain.model.Device;
import com.devices.devicesapi.infrastructure.adapter.persistence.entity.DeviceEntity;

public class DeviceEntityMapper {

    public static DeviceEntity toEntity(Device device) {
        return DeviceEntity.builder()
                .id(device.getId())
                .name(device.getName())
                .brand(device.getBrand())
                .state(device.getState())
                .creationTime(device.getCreationTime())
                .build();
    }

    public static Device toDomain(DeviceEntity entity) {
        return new Device(
                entity.getId(),
                entity.getName(),
                entity.getBrand(),
                entity.getState(),
                entity.getCreationTime()
        );
    }
}
