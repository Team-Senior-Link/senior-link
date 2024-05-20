package com.cteam.seniorlink.schedule;

import com.cteam.seniorlink.serviceBoard.ServiceEntity;
import com.cteam.seniorlink.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.mapping.ToOne;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Table(name = "schedule")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity carereceiver;  // 로그인한 user

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity caregiver; // 서비스 글 작성자

    private long serviceId; //service 테이블 serviceId

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate; // 시작 날짜

    @Column(name = "end_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate; // 종료 날짜

    @Column(length = 3000)
    private String requestMsg; // 요청 메시지

    private int status; // 예약 상태 (0: 대기중, 1: 수락)

    public ScheduleEntity toEntity(ScheduleDto s){
        return ScheduleEntity.builder()
                .scheduleId(s.getScheduleId())
                .carereceiver(s.getCarereceiver())
                .caregiver(s.getCaregiver())
                .serviceId(s.getServiceId())
                .startDate(s.getStartDate())
                .endDate(s.getEndDate())
                .requestMsg(s.getRequestMsg())
                .status(s.getStatus())
                .build();
    }
}
