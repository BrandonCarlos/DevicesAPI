package com.devices.devicesapi.application.service;

import com.devices.devicesapi.application.dto.DeviceResponse;
import com.devices.devicesapi.application.port.out.DeviceRepositoryPort;
import com.devices.devicesapi.domain.exception.InvalidOperationException;
import com.devices.devicesapi.domain.exception.NotFoundException;
import com.devices.devicesapi.domain.model.Device;
import com.devices.devicesapi.domain.model.DeviceState;
import com.devices.devicesapi.infrastructure.adapter.in.rest.dto.DeviceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateDeviceServiceTest {

    @Mock
    private DeviceRepositoryPort repository;

    @InjectMocks
    private UpdateDeviceService service;

    private UUID deviceId;
    private Device existingDevice;

    @BeforeEach
    void setUp() {
        deviceId = UUID.randomUUID();
        existingDevice = new Device(
                deviceId,
                "OldName",
                "OldBrand",
                DeviceState.IN_USE,
                LocalDateTime.now()
        );
    }

    @Test
    void shouldUpdateDeviceStateToInUseWithoutChangingNameOrBrand() {
        DeviceRequest request = new DeviceRequest();
        request.setName("OldName");
        request.setBrand("OldBrand");
        request.setState(DeviceState.IN_USE);

        Device updatedDevice = new Device(
                deviceId,
                "OldName",
                "OldBrand",
                DeviceState.IN_USE,
                existingDevice.getCreationTime()
        );

        when(repository.findById(deviceId)).thenReturn(Optional.of(existingDevice));
        when(repository.save(existingDevice)).thenReturn(updatedDevice);

        DeviceResponse response = service.update(deviceId, request);

        verify(repository).findById(deviceId);
        verify(repository).save(existingDevice);

        assertThat(response.name()).isEqualTo("OldName");
        assertThat(response.brand()).isEqualTo("OldBrand");
        assertThat(response.state()).isEqualTo(DeviceState.IN_USE);
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentDevice() {
        DeviceRequest request = new DeviceRequest();
        request.setName("SomeName");
        request.setBrand("SomeBrand");
        request.setState(DeviceState.INACTIVE);

        when(repository.findById(deviceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(deviceId, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Device not found");

        verify(repository, never()).save(any());
    }

    @Test
    void shouldPatchDeviceStateToInUseWithoutChangingNameOrBrand() {
        Map<String, Object> updates = Map.of(
                "state", "IN_USE"
        );

        Device patchedDevice = new Device(
                deviceId,
                existingDevice.getName(),
                existingDevice.getBrand(),
                DeviceState.IN_USE,
                existingDevice.getCreationTime()
        );

        when(repository.findById(deviceId)).thenReturn(Optional.of(existingDevice));
        when(repository.save(existingDevice)).thenReturn(patchedDevice);

        DeviceResponse response = service.patch(deviceId, updates);

        verify(repository).findById(deviceId);
        verify(repository).save(existingDevice);

        assertThat(response.name()).isEqualTo(existingDevice.getName());
        assertThat(response.brand()).isEqualTo(existingDevice.getBrand());
        assertThat(response.state()).isEqualTo(DeviceState.IN_USE);
    }

    @Test
    void shouldThrowWhenPatchingNonexistentDevice() {
        when(repository.findById(deviceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.patch(deviceId, Map.of("name", "New")))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Device not found");

        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowWhenPatchingCreationTime() {
        Map<String, Object> updates = Map.of("creationTime", "2023-01-01T00:00:00");
        when(repository.findById(deviceId)).thenReturn(Optional.of(existingDevice));

        assertThatThrownBy(() -> service.patch(deviceId, updates))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("Cannot update creation time");

        verify(repository, never()).save(any());
    }

    @Test
    void patch_shouldUpdateNameOnly() {
        Map<String, Object> updates = Map.of("name", "NewName");

        existingDevice = new Device(deviceId, "OldName", "OldBrand", DeviceState.AVAILABLE, LocalDateTime.now());

        when(repository.findById(deviceId)).thenReturn(Optional.of(existingDevice));
        when(repository.save(existingDevice)).thenAnswer(i -> i.getArgument(0));

        DeviceResponse response = service.patch(deviceId, updates);

        assertThat(response.name()).isEqualTo("NewName");
        assertThat(response.brand()).isEqualTo(existingDevice.getBrand());
        verify(repository).save(existingDevice);
    }

    @Test
    void patch_shouldUpdateBrandOnly() {
        Map<String, Object> updates = Map.of("brand", "NewBrand");

        existingDevice = new Device(deviceId, "OldName", "OldBrand", DeviceState.AVAILABLE, LocalDateTime.now());

        when(repository.findById(deviceId)).thenReturn(Optional.of(existingDevice));
        when(repository.save(existingDevice)).thenAnswer(i -> i.getArgument(0));

        DeviceResponse response = service.patch(deviceId, updates);

        assertThat(response.brand()).isEqualTo("NewBrand");
        assertThat(response.name()).isEqualTo(existingDevice.getName());
        verify(repository).save(existingDevice);
    }

    @Test
    void patch_shouldThrowExceptionWhenUpdatingNameOrBrandOfInUseDevice() {
        Map<String, Object> updates = Map.of(
                "name", "NewName",
                "brand", "NewBrand"
        );

        existingDevice = new Device(deviceId, "OldName", "OldBrand", DeviceState.IN_USE, LocalDateTime.now());
        when(repository.findById(deviceId)).thenReturn(Optional.of(existingDevice));

        assertThatThrownBy(() -> service.patch(deviceId, updates))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("Cannot update name or brand of a device that is in use");
    }
}