package com.cteam.seniorlink.certification;

import com.cteam.seniorlink.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@Table(name = "certification")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CertificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificationId;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity caregiver;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    private String workContract; // 근로 계약서

    @Column
    private String certificate; // 요양보호사 자격증

    @Column
    private String healthCheckup; // 건강검진 진단서

    @Column
    private String criminalHistory; // 범죄 경력 조회 회신서

    @Column
    private String privacyConsent; // 개인정보동의서

    public CertificationEntity toEntity(CertificationDto c){
        return CertificationEntity.builder()
                .certificationId(c.getCertificationId())
                .caregiver(c.getCaregiver())
                .createdAt(c.getCreatedAt())
                .workContract(c.getWorkContract())
                .certificate(c.getCertificate())
                .healthCheckup(c.getHealthCheckup())
                .criminalHistory(c.getCriminalHistory())
                .privacyConsent(c.getPrivacyConsent())
                .build();
    }
}
