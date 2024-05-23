package com.cteam.seniorlink.schedule;

import com.cteam.seniorlink.serviceBoard.ServiceEntity;
import com.cteam.seniorlink.user.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleDto {

    private long scheduleId;

    private UserEntity carereceiver;

    private UserEntity caregiver;

    private long serviceId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String requestMsg;

    private int status;

    public static ScheduleDto toDto(ScheduleEntity s){
        return ScheduleDto.builder()
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

