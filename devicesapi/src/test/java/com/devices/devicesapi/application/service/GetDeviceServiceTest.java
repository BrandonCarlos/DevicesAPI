package com.devices.devicesapi.application.service;

import com.devices.devicesapi.application.dto.DeviceResponse;
import com.devices.devicesapi.application.port.out.DeviceCachePort;
import com.devices.devicesapi.application.port.out.DeviceRepositoryPort;
import com.devices.devicesapi.domain.exception.NotFoundException;
import com.devices.devicesapi.domain.model.Device;
import com.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetDeviceServiceTest {

    @Mock
    private DeviceRepositoryPort repository;

    @Mock
    private DeviceCachePort cache;

    @InjectMocks
    private GetDeviceService service;

    private UUID deviceId;
    private Device device;

    @BeforeEach
    void setUp() {
        deviceId = UUID.randomUUID();
        device = new Device("TestDevice", "TestBrand", DeviceState.IN_USE);
    }

    @Test
    void shouldReturnDeviceFromCacheWhenPresent() {
        when(cache.get(deviceId)).thenReturn(Optional.of(device));

        DeviceResponse response = service.getById(deviceId);

        verify(cache).get(deviceId);
        verify(repository, never()).findById(any());
        assertThat(response.name()).isEqualTo(device.getName());
        assertThat(response.brand()).isEqualTo(device.getBrand());
        assertThat(response.state()).isEqualTo(device.getState());
    }

    @Test
    void shouldReturnDeviceFromRepositoryWhenNotInCache() {
        when(cache.get(deviceId)).thenReturn(Optional.empty());
        when(repository.findById(deviceId)).thenReturn(Optional.of(device));

        DeviceResponse response = service.getById(deviceId);

        verify(cache).get(deviceId);
        verify(repository).findById(deviceId);
        verify(cache).put(device);
        assertThat(response.name()).isEqualTo(device.getName());
        assertThat(response.brand()).isEqualTo(device.getBrand());
        assertThat(response.state()).isEqualTo(device.getState());
    }

    @Test
    void shouldThrowExceptionIfDeviceNotFound() {
        when(cache.get(deviceId)).thenReturn(Optional.empty());
        when(repository.findById(deviceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(deviceId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Device not found");

        verify(cache).get(deviceId);
        verify(repository).findById(deviceId);
        verify(cache, never()).put(any());
    }

    @Test
    void shouldReturnAllDevices() {
        List<Device> devices = List.of(
                new Device("Device 1", "Brand A", DeviceState.AVAILABLE),
                new Device("Device 2", "Brand B", DeviceState.IN_USE)
        );
        when(repository.findAll()).thenReturn(devices);

        List<DeviceResponse> responses = service.getAll();

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).name()).isEqualTo("Device 1");
        assertThat(responses.get(1).brand()).isEqualTo("Brand B");
    }

    @Test
    void shouldReturnDevicesByBrand() {
        String brand = "Brand A";
        List<Device> devices = List.of(
                new Device("Device A1", brand, DeviceState.IN_USE),
                new Device("Device A2", brand, DeviceState.AVAILABLE)
        );
        when(repository.findByBrand(brand)).thenReturn(devices);

        List<DeviceResponse> responses = service.getByBrand(brand);

        assertThat(responses).hasSize(2);
        assertThat(responses).allMatch(resp -> resp.brand().equals(brand));
    }
}
