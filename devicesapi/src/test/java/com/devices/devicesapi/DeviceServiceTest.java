package com.devices.devicesapi;

import com.devices.devicesapi.application.dto.DeviceRequest;
import com.devices.devicesapi.application.dto.DeviceResponse;
import com.devices.devicesapi.application.port.out.DeviceRepositoryPort;
import com.devices.devicesapi.application.service.DeviceService;
import com.devices.devicesapi.domain.exception.InvalidOperationException;
import com.devices.devicesapi.domain.exception.NotFoundException;
import com.devices.devicesapi.domain.model.Device;
import com.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeviceServiceTest {

    private DeviceRepositoryPort repository;
    private DeviceService service;

    @BeforeEach
    void setUp() {
        repository = mock(DeviceRepositoryPort.class);
        service = new DeviceService(repository);
    }

    @Test
    void create_shouldSaveAndReturnDevice() {
        DeviceRequest request = new DeviceRequest("Device1", "BrandA", DeviceState.AVAILABLE);
        Device savedDevice = new Device(request.name(), request.brand(), request.state());

        when(repository.save(any(Device.class))).thenReturn(savedDevice);

        DeviceResponse response = service.create(request);

        assertThat(response.name()).isEqualTo("Device1");
        verify(repository).save(any(Device.class));
    }

    @Test
    void getById_existingDevice_returnsDeviceResponse() {
        UUID id = UUID.randomUUID();
        Device device = new Device(id, "Name", "Brand", DeviceState.AVAILABLE, LocalDateTime.now());

        when(repository.findById(id)).thenReturn(Optional.of(device));

        DeviceResponse response = service.getById(id);

        assertThat(response.id()).isEqualTo(id);
        assertThat(response.name()).isEqualTo("Name");
    }

    @Test
    void getById_nonExistingDevice_throwsNotFoundException() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Device not found");
    }

    @Test
    void update_deviceInUse_cannotChangeNameOrBrand() {
        UUID id = UUID.randomUUID();
        Device existing = new Device(id, "OldName", "OldBrand", DeviceState.IN_USE, LocalDateTime.now());

        when(repository.findById(id)).thenReturn(Optional.of(existing));

        DeviceRequest updateRequest = new DeviceRequest("NewName", "NewBrand", DeviceState.AVAILABLE);

        assertThatThrownBy(() -> service.update(id, updateRequest))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("Cannot update name or brand of a device in use");
    }

    @Test
    void update_validUpdate_shouldSaveAndReturnUpdated() {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        Device existing = new Device(id, "OldName", "OldBrand", DeviceState.AVAILABLE, created);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(Device.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DeviceRequest updateRequest = new DeviceRequest("NewName", "OldBrand", DeviceState.IN_USE);

        DeviceResponse response = service.update(id, updateRequest);

        assertThat(response.name()).isEqualTo("NewName");
        assertThat(response.brand()).isEqualTo("OldBrand");
        assertThat(response.state()).isEqualTo(DeviceState.IN_USE);
    }

    @Test
    void delete_deviceInUse_throwsInvalidOperationException() {
        UUID id = UUID.randomUUID();
        Device existing = new Device(id, "Device", "Brand", DeviceState.IN_USE, LocalDateTime.now());

        when(repository.findById(id)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("Cannot delete a device that is in use");
    }

    @Test
    void delete_existingDevice_shouldCallRepositoryDelete() {
        UUID id = UUID.randomUUID();
        Device existing = new Device(id, "Device", "Brand", DeviceState.AVAILABLE, LocalDateTime.now());

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(repository).deleteById(id);

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }
}
