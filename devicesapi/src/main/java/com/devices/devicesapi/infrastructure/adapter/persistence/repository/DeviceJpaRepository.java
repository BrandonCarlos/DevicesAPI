package com.devices.devicesapi.infrastructure.adapter.persistence.repository;

import com.devices.devicesapi.domain.model.DeviceState;
import com.devices.devicesapi.infrastructure.adapter.persistence.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeviceJpaRepository extends JpaRepository<DeviceEntity, UUID> {
    List<DeviceEntity> findByBrand(String brand);
    List<DeviceEntity> findByState(DeviceState state);
}
