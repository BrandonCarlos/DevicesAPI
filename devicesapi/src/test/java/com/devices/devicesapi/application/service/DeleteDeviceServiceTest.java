package com.devices.devicesapi.application.service;

import com.devices.devicesapi.application.port.out.DeviceCachePort;
import com.devices.devicesapi.application.port.out.DeviceRepositoryPort;
import com.devices.devicesapi.domain.exception.InvalidOperationException;
import com.devices.devicesapi.domain.model.Device;
import com.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteDeviceServiceTest {

    @Mock
    private DeviceRepositoryPort repository;

    @Mock
    private DeviceCachePort cache;

    @InjectMocks
    private DeleteDeviceService service;

    private UUID deviceId;

    @BeforeEach
    void setUp() {
        deviceId = UUID.randomUUID();
    }

    @Test
    void shouldDeleteDeviceIfNotInUse() {
        Device device = new Device("Device A", "Brand X", DeviceState.INACTIVE);
        when(repository.findById(deviceId)).thenReturn(Optional.of(device));

        service.delete(deviceId);

        verify(repository).findById(deviceId);
        verify(repository).deleteById(deviceId);
        verify(cache).evict(deviceId);
    }

    @Test
    void shouldThrowExceptionIfDeviceNotFound() {
        when(repository.findById(deviceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(deviceId))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("Device not found");

        verify(repository, never()).deleteById(any());
        verify(cache, never()).evict(any());
    }

    @Test
    void shouldThrowExceptionIfDeviceInUse() {
        Device device = new Device("Device B", "Brand Y", DeviceState.IN_USE);
        when(repository.findById(deviceId)).thenReturn(Optional.of(device));

        assertThatThrownBy(() -> service.delete(deviceId))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("Cannot delete a device that is in use");

        verify(repository).findById(deviceId);
        verify(repository, never()).deleteById(any());
        verify(cache, never()).evict(any());
    }
}