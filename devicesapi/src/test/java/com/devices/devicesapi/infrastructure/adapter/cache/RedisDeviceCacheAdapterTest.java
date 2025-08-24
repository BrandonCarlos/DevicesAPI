package com.devices.devicesapi.infrastructure.adapter.cache;

import com.devices.devicesapi.domain.model.Device;
import com.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisDeviceCacheAdapterTest {

    @Mock
    private RedisTemplate<String, Device> redisTemplate;

    @Mock
    private ValueOperations<String, Device> valueOperations;

    private RedisDeviceCacheAdapter cacheAdapter;

    private UUID deviceId;
    private Device device;

    @BeforeEach
    void setUp() {
        cacheAdapter = new RedisDeviceCacheAdapter(redisTemplate);

        deviceId = UUID.randomUUID();
        device = new Device(
                deviceId,
                "TestDevice",
                "TestBrand",
                DeviceState.IN_USE,
                LocalDateTime.now()
        );

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void shouldReturnDeviceFromCacheIfPresent() {
        String key = "device:" + deviceId;

        when(valueOperations.get(key)).thenReturn(device);

        Optional<Device> result = cacheAdapter.get(deviceId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(device);
        verify(redisTemplate).opsForValue();
        verify(valueOperations).get(key);
    }

    @Test
    void shouldReturnEmptyIfDeviceNotInCache() {
        String key = "device:" + deviceId;

        when(valueOperations.get(key)).thenReturn(null);

        Optional<Device> result = cacheAdapter.get(deviceId);

        assertThat(result).isEmpty();
        verify(redisTemplate).opsForValue();
        verify(valueOperations).get(key);
    }

    @Test
    void shouldPutDeviceInCache() {
        String key = "device:" + deviceId;

        cacheAdapter.put(device);

        verify(redisTemplate).opsForValue();
        verify(valueOperations).set(key, device);
    }

    @Test
    void shouldEvictDeviceFromCache() {
        String key = "device:" + deviceId;

        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        cacheAdapter.evict(deviceId);

        verify(redisTemplate).delete(key);
    }
}