package com.devices.devicesapi.application.service;

import com.devices.devicesapi.application.dto.DeviceRequest;
import com.devices.devicesapi.application.dto.DeviceResponse;
import com.devices.devicesapi.application.port.out.DeviceRepositoryPort;
import com.devices.devicesapi.domain.model.Device;
import com.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDeviceServiceTest {

    @Mock
    private DeviceRepositoryPort repository;

    @InjectMocks
    private CreateDeviceService service;

    private DeviceRequest request;
    private Device device;
    private Device savedDevice;

    @BeforeEach
    void setUp() {
        request = new DeviceRequest("Phone X", "BrandY", DeviceState.IN_USE);
        device = new Device("Phone X", "BrandY", DeviceState.IN_USE);
        savedDevice = new Device("Phone X", "BrandY", DeviceState.IN_USE);
    }

    @Test
    void shouldCreateDeviceSuccessfully() {
        when(repository.save(any(Device.class))).thenReturn(savedDevice);

        DeviceResponse response = service.create(request);

        verify(repository).save(any(Device.class));
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo(request.name());
        assertThat(response.brand()).isEqualTo(request.brand());
        assertThat(response.state()).isEqualTo(request.state());
    }
}