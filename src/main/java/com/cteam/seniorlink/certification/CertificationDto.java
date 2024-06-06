package com.cteam.seniorlink.certification;

import com.cteam.seniorlink.user.UserEntity;
import jakarta.persistence.Column;
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

    private LocalDateTime createdAt;

    private String workContract; // 근로 계약서

    private String certificate; // 요양보호사 자격증

    private String healthCheckup; // 건강검진 진단서

    private String criminalHistory; // 범죄 경력 조회 회신서

    private String privacyConsent; // 개인정보동의서

    private MultipartFile workContractFile;

    private MultipartFile certificateFile;

    private MultipartFile healthCheckupFile;

    private MultipartFile criminalHistoryFile;

    private MultipartFile privacyConsentFile;

    public static CertificationDto toDto(CertificationEntity c) {
        return CertificationDto.builder()
                .certificationId(c.getCertificationId())
                .caregiver(c.getCaregiver())
                .createdAt(c.getCreatedAt())
                .workContract(c.getWorkContract())
                .certificate(c.getCertificate())
                .healthCheckup(c.getHealthCheckup())
                .criminalHistory(c.getCriminalHistory())
                .privacyConsent(c.getPrivacyConsent())
                .workContractFile(null)
                .certificateFile(null)
                .healthCheckupFile(null)
                .criminalHistoryFile(null)
                .privacyConsentFile(null)
                .build();
    }
}
