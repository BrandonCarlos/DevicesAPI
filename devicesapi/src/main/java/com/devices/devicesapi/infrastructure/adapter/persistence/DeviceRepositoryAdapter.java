package com.devices.devicesapi.infrastructure.adapter.persistence;

import com.devices.devicesapi.application.port.out.DeviceRepositoryPort;
import com.devices.devicesapi.domain.exception.NotFoundException;
import com.devices.devicesapi.domain.model.Device;
import com.devices.devicesapi.domain.model.DeviceState;
import com.devices.devicesapi.infrastructure.adapter.persistence.entity.DeviceEntity;
import com.devices.devicesapi.infrastructure.adapter.persistence.repository.DeviceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DeviceRepositoryAdapter implements DeviceRepositoryPort {

    private final DeviceJpaRepository jpaRepository;

    private Device toDomain(DeviceEntity entity) {
        return new Device(
                entity.getId(),
                entity.getName(),
                entity.getBrand(),
                entity.getState(),
                entity.getCreationTime()
        );
    }

    private DeviceEntity toEntity(Device domain) {
        return new DeviceEntity(
                domain.getId(),
                domain.getName(),
                domain.getBrand(),
                domain.getState(),
                domain.getCreationTime()
        );
    }

    @Override
    public Device save(Device device) {
        DeviceEntity entity = toEntity(device);
        DeviceEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Device> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Device> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Device> findByBrand(String brand) {
        return jpaRepository.findByBrand(brand).stream()
                .map(entity -> {
                    return new Device(
                            entity.getId(),
                            entity.getName(),
                            entity.getBrand(),
                            DeviceState.valueOf(String.valueOf(entity.getState())),
                            entity.getCreationTime()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Device> findByState(DeviceState state) {
        return jpaRepository.findByState(state).stream()
                .map(entity -> {
                    return new Device(
                            entity.getId(),
                            entity.getName(),
                            entity.getBrand(),
                            DeviceState.valueOf(String.valueOf(entity.getState())),
                            entity.getCreationTime()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID deviceId) {
        if (!jpaRepository.existsById(deviceId)) {
            throw new NotFoundException("Device with id " + deviceId + " not found");
        }
        jpaRepository.deleteById(deviceId);
    }
}
