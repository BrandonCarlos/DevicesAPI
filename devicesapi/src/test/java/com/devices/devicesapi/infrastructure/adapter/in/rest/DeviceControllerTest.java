package com.devices.devicesapi.infrastructure.adapter.in.rest;

import com.devices.devicesapi.application.dto.DeviceRequest;
import com.devices.devicesapi.application.dto.DeviceResponse;
import com.devices.devicesapi.application.port.in.DeviceUseCase;
import com.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeviceControllerTest {

    @Mock
    private DeviceUseCase deviceUseCase;

    @InjectMocks
    private DeviceController deviceController;

    private DeviceRequest deviceRequest;
    private DeviceResponse deviceResponse;
    private UUID deviceId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        deviceId = UUID.randomUUID();

        deviceRequest = new DeviceRequest("Test Device", "Acme", DeviceState.IN_USE);

        deviceResponse = new DeviceResponse(deviceId, "Test Device", "Acme", DeviceState.AVAILABLE, LocalDateTime.now());
    }

    @Test
    void testCreate() {
        when(deviceUseCase.create(deviceRequest)).thenReturn(deviceResponse);

        DeviceResponse response = deviceController.create(deviceRequest);

        assertNotNull(response);
        assertEquals(deviceResponse.id(), response.id());
        verify(deviceUseCase, times(1)).create(deviceRequest);
    }

    @Test
    void testGetById() {
        when(deviceUseCase.getById(deviceId)).thenReturn(deviceResponse);

        DeviceResponse response = deviceController.getById(deviceId);

        assertNotNull(response);
        assertEquals(deviceResponse.id(), response.id());
        verify(deviceUseCase, times(1)).getById(deviceId);
    }

    @Test
    void testGetAll() {
        List<DeviceResponse> list = Collections.singletonList(deviceResponse);
        when(deviceUseCase.getAll()).thenReturn(list);

        List<DeviceResponse> responseList = deviceController.getAll();

        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        verify(deviceUseCase, times(1)).getAll();
    }

    @Test
    void testGetByBrand() {
        String brand = "Acme";
        List<DeviceResponse> list = Collections.singletonList(deviceResponse);
        when(deviceUseCase.getByBrand(brand)).thenReturn(list);

        List<DeviceResponse> responseList = deviceController.getByBrand(brand);

        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        verify(deviceUseCase, times(1)).getByBrand(brand);
    }

    @Test
    void testGetByState() {
        DeviceState state = DeviceState.IN_USE;
        List<DeviceResponse> list = Collections.singletonList(deviceResponse);
        when(deviceUseCase.getByState(state)).thenReturn(list);

        List<DeviceResponse> responseList = deviceController.getByState(state);

        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        verify(deviceUseCase, times(1)).getByState(state);
    }

    @Test
    void testUpdate() {
        when(deviceUseCase.update(deviceId, deviceRequest)).thenReturn(deviceResponse);

        DeviceResponse response = deviceController.update(deviceId, deviceRequest);

        assertNotNull(response);
        assertEquals(deviceResponse.id(), response.id());
        verify(deviceUseCase, times(1)).update(deviceId, deviceRequest);
    }

    @Test
    void testPatch() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "Updated Device");

        when(deviceUseCase.patch(deviceId, updates)).thenReturn(deviceResponse);

        DeviceResponse response = deviceController.patch(deviceId, updates);

        assertNotNull(response);
        verify(deviceUseCase, times(1)).patch(deviceId, updates);
    }

    @Test
    void testDelete() {
        doNothing().when(deviceUseCase).delete(deviceId);

        deviceController.delete(deviceId);

        verify(deviceUseCase, times(1)).delete(deviceId);
    }
}