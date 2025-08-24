package com.devices.devicesapi.infrastructure.adapter.cache;

import com.devices.devicesapi.application.port.out.DeviceCachePort;
import com.devices.devicesapi.domain.model.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RedisDeviceCacheAdapter implements DeviceCachePort {

    private final RedisTemplate<String, Device> redisTemplate;

    private static final String PREFIX = "device:";

    @Override
    public Optional<Device> get(UUID id) {
        Device device = redisTemplate.opsForValue().get(PREFIX + id.toString());
        return Optional.ofNullable(device);
    }

    @Override
    public void put(Device device) {
        redisTemplate.opsForValue().set(PREFIX + device.getId().toString(), device);
    }

    @Override
    public void evict(UUID id) {
        redisTemplate.delete(PREFIX + id.toString());
    }
}
