package com.devices.devicesapi.infrastructure.adapter.persistence.mapper;

import com.devices.devicesapi.domain.exception.NotFoundException;
import com.devices.devicesapi.domain.model.Device;
import com.devices.devicesapi.domain.model.DeviceState;
import com.devices.devicesapi.infrastructure.adapter.persistence.DeviceRepositoryAdapter;
import com.devices.devicesapi.infrastructure.adapter.persistence.entity.DeviceEntity;
import com.devices.devicesapi.infrastructure.adapter.persistence.repository.DeviceJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.devices.devicesapi.infrastructure.adapter.persistence.mapper.DeviceEntityMapper.toEntity;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceRepositoryAdapterTest {

    @Mock
    private DeviceJpaRepository jpaRepository;

    private DeviceRepositoryAdapter adapter;

    private UUID deviceId;
    private Device device;
    private DeviceEntity deviceEntity;

    @BeforeEach
    void setUp() {
        adapter = new DeviceRepositoryAdapter(jpaRepository);

        deviceId = UUID.randomUUID();
        device = new Device(deviceId, "DeviceName", "DeviceBrand", DeviceState.IN_USE, LocalDateTime.now());

        deviceEntity = toEntity(device);
    }

    @Test
    void shouldSaveDevice() {
        when(jpaRepository.save(any(DeviceEntity.class))).thenReturn(deviceEntity);

        Device saved = adapter.save(device);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(device.getId());
        assertThat(saved.getName()).isEqualTo(device.getName());
        assertThat(saved.getBrand()).isEqualTo(device.getBrand());
        assertThat(saved.getState()).isEqualTo(device.getState());

        ArgumentCaptor<DeviceEntity> captor = ArgumentCaptor.forClass(DeviceEntity.class);
        verify(jpaRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(device.getId());
    }

    @Test
    void shouldFindByIdWhenPresent() {
        when(jpaRepository.findById(deviceId)).thenReturn(Optional.of(deviceEntity));

        Optional<Device> found = adapter.findById(deviceId);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(deviceId);
        assertThat(found.get().getName()).isEqualTo(device.getName());
    }

    @Test
    void shouldReturnEmptyWhenFindByIdNotFound() {
        when(jpaRepository.findById(deviceId)).thenReturn(Optional.empty());

        Optional<Device> found = adapter.findById(deviceId);

        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindAllDevices() {
        List<DeviceEntity> entities = List.of(deviceEntity);
        when(jpaRepository.findAll()).thenReturn(entities);

        List<Device> devices = adapter.findAll();

        assertThat(devices).hasSize(1);
        assertThat(devices.get(0).getId()).isEqualTo(deviceId);
    }

    @Test
    void shouldFindDevicesByBrand() {
        List<DeviceEntity> entities = List.of(deviceEntity);
        when(jpaRepository.findByBrand("DeviceBrand")).thenReturn(entities);

        List<Device> devices = adapter.findByBrand("DeviceBrand");

        assertThat(devices).hasSize(1);
        assertThat(devices.get(0).getBrand()).isEqualTo("DeviceBrand");
    }

    @Test
    void shouldFindDevicesByState() {
        List<DeviceEntity> entities = List.of(deviceEntity);
        when(jpaRepository.findByState(DeviceState.IN_USE)).thenReturn(entities);

        List<Device> devices = adapter.findByState(DeviceState.IN_USE);

        assertThat(devices).hasSize(1);
        assertThat(devices.get(0).getState()).isEqualTo(DeviceState.IN_USE);
    }

    @Test
    void shouldDeleteDeviceByIdIfExists() {
        when(jpaRepository.existsById(deviceId)).thenReturn(true);
        doNothing().when(jpaRepository).deleteById(deviceId);

        adapter.deleteById(deviceId);

        verify(jpaRepository).existsById(deviceId);
        verify(jpaRepository).deleteById(deviceId);
    }

    @Test
    void shouldThrowWhenDeleteByIdIfNotExists() {
        when(jpaRepository.existsById(deviceId)).thenReturn(false);

        assertThatThrownBy(() -> adapter.deleteById(deviceId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(deviceId.toString());

        verify(jpaRepository).existsById(deviceId);
        verify(jpaRepository, never()).deleteById(any());
    }
}