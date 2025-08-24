package com.devices.devicesapi.application.service;

import com.devices.devicesapi.application.dto.DeviceRequest;
import com.devices.devicesapi.application.dto.DeviceResponse;
import com.devices.devicesapi.application.port.out.DeviceRepositoryPort;
import com.devices.devicesapi.domain.exception.NotFoundException;
import com.devices.devicesapi.domain.exception.InvalidOperationException;
import com.devices.devicesapi.domain.model.Device;
import com.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeviceServiceTest {

    @Mock
    private DeviceRepositoryPort repository;

    @InjectMocks
    private DeviceService service;

    private UUID deviceId;
    private Device existingDevice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deviceId = UUID.randomUUID();
        existingDevice = new Device(deviceId, "ExistingName", "ExistingBrand", DeviceState.AVAILABLE, LocalDateTime.now().minusDays(1));
    }

    @Test
    void create_shouldSaveAndReturnDevice() {
        DeviceRequest request = new DeviceRequest("Name", "Brand", DeviceState.AVAILABLE);

        when(repository.save(any(Device.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DeviceResponse response = service.create(request);

        verify(repository).save(any(Device.class));
        assertThat(response.name()).isEqualTo("Name");
        assertThat(response.brand()).isEqualTo("Brand");
        assertThat(response.state()).isEqualTo(DeviceState.AVAILABLE);
        assertThat(response.id()).isNotNull();
        assertThat(response.creationTime()).isNotNull();
    }

    @Test
    void update_shouldUpdateDevice_whenFound() {
        DeviceRequest request = new DeviceRequest("UpdatedName", "UpdatedBrand", DeviceState.IN_USE);
        Device updatedDevice = new Device(deviceId, "UpdatedName", "UpdatedBrand", DeviceState.IN_USE, existingDevice.getCreationTime());

        when(repository.findById(deviceId)).thenReturn(Optional.of(existingDevice));
        when(repository.save(existingDevice)).thenReturn(updatedDevice);

        DeviceResponse response = service.update(deviceId, request);

        verify(repository).findById(deviceId);
        verify(repository).save(existingDevice);

        assertThat(response.name()).isEqualTo("UpdatedName");
        assertThat(response.brand()).isEqualTo("UpdatedBrand");
        assertThat(response.state()).isEqualTo(DeviceState.IN_USE);
    }

    @Test
    void update_shouldThrowNotFoundException_whenDeviceNotFound() {
        when(repository.findById(deviceId)).thenReturn(Optional.empty());

        DeviceRequest request = new DeviceRequest("Name", "Brand", DeviceState.AVAILABLE);

        assertThatThrownBy(() -> service.update(deviceId, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Device not found");

        verify(repository).findById(deviceId);
        verify(repository, never()).save(any());
    }

    @Test
    void patch_shouldPartiallyUpdateDevice_whenFound() {
        Map<String, Object> updates = Map.of(
                "name", "PatchedName",
                "state", "IN_USE"
        );

        Device patchedDevice = new Device(deviceId, "PatchedName", existingDevice.getBrand(), DeviceState.IN_USE, existingDevice.getCreationTime());

        when(repository.findById(deviceId)).thenReturn(Optional.of(existingDevice));
        when(repository.save(existingDevice)).thenReturn(patchedDevice);

        DeviceResponse response = service.patch(deviceId, updates);

        verify(repository).findById(deviceId);
        verify(repository).save(existingDevice);

        assertThat(response.name()).isEqualTo("PatchedName");
        assertThat(response.brand()).isEqualTo(existingDevice.getBrand());
        assertThat(response.state()).isEqualTo(DeviceState.IN_USE);
    }

    @Test
    void patch_shouldThrowNotFoundException_whenDeviceNotFound() {
        when(repository.findById(deviceId)).thenReturn(Optional.empty());

        Map<String, Object> updates = Map.of("name", "Name");

        assertThatThrownBy(() -> service.patch(deviceId, updates))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Device not found");

        verify(repository).findById(deviceId);
        verify(repository, never()).save(any());
    }

    @Test
    void getById_shouldReturnDevice_whenFound() {
        when(repository.findById(deviceId)).thenReturn(Optional.of(existingDevice));

        DeviceResponse response = service.getById(deviceId);

        verify(repository).findById(deviceId);
        assertThat(response.id()).isEqualTo(deviceId);
        assertThat(response.name()).isEqualTo(existingDevice.getName());
    }

    @Test
    void getById_shouldThrowNotFoundException_whenNotFound() {
        when(repository.findById(deviceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(deviceId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Device not found");
    }

    @Test
    void getAll_shouldReturnAllDevices() {
        List<Device> devices = List.of(
                existingDevice,
                new Device(UUID.randomUUID(), "Name2", "Brand2", DeviceState.IN_USE, LocalDateTime.now())
        );

        when(repository.findAll()).thenReturn(devices);

        List<DeviceResponse> responses = service.getAll();

        verify(repository).findAll();
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).id()).isEqualTo(existingDevice.getId());
    }

    @Test
    void getByBrand_shouldReturnDevicesByBrand() {
        List<Device> devices = List.of(existingDevice);
        when(repository.findByBrand("ExistingBrand")).thenReturn(devices);

        List<DeviceResponse> responses = service.getByBrand("ExistingBrand");

        verify(repository).findByBrand("ExistingBrand");
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).brand()).isEqualTo("ExistingBrand");
    }

    @Test
    void getByState_shouldReturnDevicesByState() {
        List<Device> devices = List.of(existingDevice);
        when(repository.findByState(DeviceState.AVAILABLE)).thenReturn(devices);

        List<DeviceResponse> responses = service.getByState(DeviceState.AVAILABLE);

        verify(repository).findByState(DeviceState.AVAILABLE);
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).state()).isEqualTo(DeviceState.AVAILABLE);
    }

    @Test
    void delete_shouldDeleteDevice_whenAllowed() {
        when(repository.findById(deviceId)).thenReturn(Optional.of(existingDevice));
        doNothing().when(repository).deleteById(deviceId);

        service.delete(deviceId);

        verify(repository).findById(deviceId);
        verify(repository).deleteById(deviceId);
    }

    @Test
    void delete_shouldThrowNotFoundException_whenDeviceNotFound() {
        when(repository.findById(deviceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(deviceId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Device not found");

        verify(repository).findById(deviceId);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void delete_shouldThrowInvalidOperationException_whenDeviceInUse() {
        Device inUseDevice = new Device(deviceId, "Name", "Brand", DeviceState.IN_USE, existingDevice.getCreationTime());

        when(repository.findById(deviceId)).thenReturn(Optional.of(inUseDevice));

        assertThatThrownBy(() -> service.delete(deviceId))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("In-use devices cannot be deleted");

        verify(repository).findById(deviceId);
        verify(repository, never()).deleteById(any());
    }
}