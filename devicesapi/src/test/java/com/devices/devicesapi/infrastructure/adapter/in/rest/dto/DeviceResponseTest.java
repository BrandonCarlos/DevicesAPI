package com.devices.devicesapi.infrastructure.adapter.in.rest.dto;

import com.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class DeviceResponseTest {

    @Test
    void shouldBuildDeviceResponseCorrectly() {
        UUID id = UUID.randomUUID();
        String name = "TestDevice";
        String brand = "TestBrand";
        DeviceState state = DeviceState.AVAILABLE;
        LocalDateTime creationTime = LocalDateTime.now();

        DeviceResponse response = DeviceResponse.builder()
                .id(id)
                .name(name)
                .brand(brand)
                .state(state)
                .creationTime(creationTime)
                .build();

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getBrand()).isEqualTo(brand);
        assertThat(response.getState()).isEqualTo(state);
        assertThat(response.getCreationTime()).isEqualTo(creationTime);
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        LocalDateTime creationTime = LocalDateTime.now();

        DeviceResponse response1 = DeviceResponse.builder()
                .id(id)
                .name("Name")
                .brand("Brand")
                .state(DeviceState.IN_USE)
                .creationTime(creationTime)
                .build();

        DeviceResponse response2 = DeviceResponse.builder()
                .id(id)
                .name("Name")
                .brand("Brand")
                .state(DeviceState.IN_USE)
                .creationTime(creationTime)
                .build();

        DeviceResponse response3 = DeviceResponse.builder()
                .id(UUID.randomUUID()) // different id
                .name("Name")
                .brand("Brand")
                .state(DeviceState.IN_USE)
                .creationTime(creationTime)
                .build();

        assertThat(response1).isEqualTo(response2);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
        assertThat(response1).isNotEqualTo(response3);
    }

    @Test
    void shouldSupportToString() {
        DeviceResponse response = DeviceResponse.builder()
                .id(UUID.randomUUID())
                .name("Name")
                .brand("Brand")
                .state(DeviceState.IN_USE)
                .creationTime(LocalDateTime.now())
                .build();

        String toString = response.toString();

        assertThat(toString).contains("DeviceResponse");
        assertThat(toString).contains("name=Name");
        assertThat(toString).contains("brand=Brand");
    }
}