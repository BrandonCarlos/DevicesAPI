package com.devices.devicesapi.infrastructure.adapter.in.rest;

import com.devices.devicesapi.application.dto.DeviceRequest;
import com.devices.devicesapi.application.dto.DeviceResponse;
import com.devices.devicesapi.application.port.in.DeviceUseCase;
import com.devices.devicesapi.domain.model.DeviceState;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceUseCase deviceUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceResponse create(@RequestBody DeviceRequest request) {
        return deviceUseCase.create(request);
    }

    @GetMapping("/{id}")
    public DeviceResponse getById(@PathVariable UUID id) {
        return deviceUseCase.getById(id);
    }

    @GetMapping
    public List<DeviceResponse> getAll() {
        return deviceUseCase.getAll();
    }

    @GetMapping(params = "brand")
    public List<DeviceResponse> getByBrand(@RequestParam String brand) {
        return deviceUseCase.getByBrand(brand);
    }

    @GetMapping(params = "state")
    public List<DeviceResponse> getByState(@RequestParam DeviceState state) {
        return deviceUseCase.getByState(state);
    }

    @PutMapping("/{id}")
    public DeviceResponse update(@PathVariable UUID id, @RequestBody DeviceRequest request) {
        return deviceUseCase.update(id, request);
    }

    @PatchMapping("/{id}")
    public DeviceResponse patch(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        return deviceUseCase.patch(id, updates);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deviceUseCase.delete(id);
    }
}
