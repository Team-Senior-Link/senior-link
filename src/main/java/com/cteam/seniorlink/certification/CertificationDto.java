package com.cteam.seniorlink.certification;

import com.cteam.seniorlink.user.UserEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificationDto {
    private Long certificationId;

    private UserEntity caregiver;

    private String name;

    private String organization;

    private LocalDateTime createdAt;

    private String imgPath;

    private MultipartFile f;

    public CertificationDto toDto(CertificationEntity c) {
        return CertificationDto.builder()
                .certificationId(c.getCertificationId())
                .caregiver(c.getCaregiver())
                .name(c.getName())
                .organization(c.getOrganization())
                .createdAt(c.getCreatedAt())
                .imgPath(c.getImgPath())
                .f(null)
                .build();
    }
}
