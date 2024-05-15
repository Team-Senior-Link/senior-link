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
    private String title; //제목

    @Column(length = 5000)
    private String introduction; //소개글

    @Column(length = 500)
    private String career; //대표경력

    @Column(length = 500)
    private String specialty; //전문분야

    @Column(length = 500)
    private String location; //활동지역

    private int timeFree; //시간당요금

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt; //게시일

    @Column(insertable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt; //수정일

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity caregiver; //작성자

    @Column(length = 500)
    private String imgPath; //활동사진

    public ServiceEntity toEntity(ServiceDto s) {
        return ServiceEntity.builder()
                .serviceId(s.getServiceId())
                .title(s.getTitle())
                .introduction(s.getIntroduction())
                .career(s.getCareer())
                .specialty(s.getSpecialty())
                .location(s.getLocation())
                .timeFree(s.getTimeFree())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .caregiver(s.getCaregiver())
                .imgPath(s.getImgPath())
                .build();
    }

}
