package com.devices.devicesapi.infrastructure.adapter.persistence.entity;


import com.devices.devicesapi.domain.model.DeviceState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;

    private String brand;

    @Enumerated(EnumType.STRING)
    private DeviceState state;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

}
