package com.cteam.seniorlink.certification;

import com.cteam.seniorlink.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Column(length = 500)
    private String name;

    @Column(length = 500)
    private String organization;

    @Column(length = 500)
    private String imgPath;

    public CertificationEntity toEntity(CertificationDto c){
        return CertificationEntity.builder()
                .certificationId(c.getCertificationId())
                .caregiver(c.getCaregiver())
                .name(c.getName())
                .organization(c.getOrganization())
                .imgPath(c.getImgPath())
                .build();
    }
}
