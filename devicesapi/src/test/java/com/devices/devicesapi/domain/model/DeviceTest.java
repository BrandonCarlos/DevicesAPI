package com.devices.devicesapi.domain.model;

import com.devices.devicesapi.domain.exception.InvalidOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class DeviceTest {

    private UUID id;
    private String name;
    private String brand;
    private DeviceState state;
    private LocalDateTime creationTime;
    private Device device;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        name = "DeviceName";
        brand = "DeviceBrand";
        state = DeviceState.AVAILABLE;
        creationTime = LocalDateTime.now().minusDays(1);
        device = new Device(id, name, brand, state, creationTime);
    }

    @Test
    void constructor_shouldInitializeFields() {
        Device newDevice = new Device("NewName", "NewBrand", DeviceState.IN_USE);

        assertThat(newDevice.getId()).isNotNull();
        assertThat(newDevice.getName()).isEqualTo("NewName");
        assertThat(newDevice.getBrand()).isEqualTo("NewBrand");
        assertThat(newDevice.getState()).isEqualTo(DeviceState.IN_USE);
        assertThat(newDevice.getCreationTime()).isNotNull();
    }

    @Test
    void update_shouldUpdateFields_whenValid() {
        Device updated = new Device(id, "UpdatedName", "UpdatedBrand", DeviceState.IN_USE, creationTime);

        device.update(updated);

        assertThat(device.getName()).isEqualTo("UpdatedName");
        assertThat(device.getBrand()).isEqualTo("UpdatedBrand");
        assertThat(device.getState()).isEqualTo(DeviceState.IN_USE);
        assertThat(device.getCreationTime()).isEqualTo(creationTime);
    }

    @Test
    void update_shouldThrow_whenChangingNameOrBrandWhileInUse() {
        device = new Device(id, name, brand, DeviceState.IN_USE, creationTime);

        Device updated = new Device(id, "NewName", "NewBrand", DeviceState.IN_USE, creationTime);

        assertThatThrownBy(() -> device.update(updated))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("Cannot update name or brand of a device that is in use");
    }

    @Test
    void update_shouldThrow_whenChangingCreationTime() {
        Device updated = new Device(id, name, brand, state, creationTime.plusDays(1));

        assertThatThrownBy(() -> device.update(updated))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("Creation time cannot be updated");
    }

    @Test
    void patch_shouldUpdateFieldsCorrectly() {
        Map<String, Object> updates = Map.of(
                "name", "PatchedName",
                "brand", "PatchedBrand",
                "state", "IN_USE"
        );

        device.patch(updates);

        assertThat(device.getName()).isEqualTo("PatchedName");
        assertThat(device.getBrand()).isEqualTo("PatchedBrand");
        assertThat(device.getState()).isEqualTo(DeviceState.IN_USE);
    }

    @Test
    void patch_shouldThrow_whenUpdatingNameOrBrandWhileInUse() {
        device = new Device(id, name, brand, DeviceState.IN_USE, creationTime);

        Map<String, Object> updates = Map.of(
                "name", "NewName"
        );

        assertThatThrownBy(() -> device.patch(updates))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("Cannot update name/brand of device in use");
    }

    @Test
    void patch_shouldThrow_whenUpdatingCreationTime() {
        Map<String, Object> updates = Map.of(
                "creationTime", LocalDateTime.now()
        );

        assertThatThrownBy(() -> device.patch(updates))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("Cannot update creation time");
    }

    @Test
    void patch_shouldThrow_whenUpdatingInvalidField() {
        Map<String, Object> updates = Map.of(
                "invalidField", "value"
        );

        assertThatThrownBy(() -> device.patch(updates))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessageContaining("Invalid field");
    }

    @Test
    void validateDeletion_shouldThrow_whenStateInUse() {
        device = new Device(id, name, brand, DeviceState.IN_USE, creationTime);

        assertThatThrownBy(() -> device.validateDeletion())
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("In-use devices cannot be deleted");
    }

    @Test
    void validateDeletion_shouldNotThrow_whenStateNotInUse() {
        device.validateDeletion();
    }
}