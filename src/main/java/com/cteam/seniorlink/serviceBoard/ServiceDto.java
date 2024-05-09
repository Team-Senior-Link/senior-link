package com.cteam.seniorlink.serviceBoard;

import com.cteam.seniorlink.user.UserEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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

    private String imgPath;

    private MultipartFile f;

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
                .imgPath(s.getImgPath())
                .f(null)
                .build();
    }
}
