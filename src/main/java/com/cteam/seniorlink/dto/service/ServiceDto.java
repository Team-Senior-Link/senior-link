package com.cteam.seniorlink.dto.service;

import com.cteam.seniorlink.domain.service.ServiceEntity;
import com.cteam.seniorlink.domain.user.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDto {
    private Long serviceId;

    private String title;

    private String introduction;

    private String career;

    private String specialty;

    private String location;

    private int dayFree;

    private int timeFree;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UserEntity caregiver;

    public ServiceDto toDto(ServiceEntity s) {
        return ServiceDto.builder()
                .serviceId(s.getServiceId())
                .title(s.getTitle())
                .introduction(s.getIntroduction())
                .career(s.getCareer())
                .specialty(s.getSpecialty())
                .location(s.getLocation())
                .dayFree(s.getDayFree())
                .timeFree(s.getTimeFree())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .caregiver(s.getCaregiver())
                .build();
    }
}
