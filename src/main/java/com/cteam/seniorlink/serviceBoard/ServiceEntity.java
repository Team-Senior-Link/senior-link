package com.cteam.seniorlink.serviceBoard;


import com.cteam.seniorlink.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Table(name = "service")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    @Column(length = 1000)
    private String title;

    @Column(length = 5000)
    private String introduction;

    @Column(length = 500)
    private String career;

    @Column(length = 500)
    private String specialty;

    @Column(length = 500)
    private String location;

    private int dayFree;

    private int timeFree;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(insertable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity caregiver;

    @Column(length = 500)
    private String imgPath;

    public ServiceEntity toEntity(ServiceDto s) {
        return ServiceEntity.builder()
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
                .build();
    }

}
