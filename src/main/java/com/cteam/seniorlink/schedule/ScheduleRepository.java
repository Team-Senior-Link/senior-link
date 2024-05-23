package com.cteam.seniorlink.schedule;

import com.cteam.seniorlink.user.UserEntity;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    List<ScheduleEntity> findByServiceId(long serviceId); // serviceId로 찾기
    List<ScheduleEntity> findByCarereceiver(UserEntity carereceiver); // carereceiver로 찾기
    List<ScheduleEntity> findByCaregiver(UserEntity caregiver); // caregiver로 찾기
    List<ScheduleEntity> findByCarereceiverAndStartDateBetween(UserEntity carereceiver, LocalDateTime startDate, LocalDateTime endDate); // carereceiver의 서비스 신청 횟수
    List<ScheduleEntity> findAllByCaregiverUserId(long userId);
    List<ScheduleEntity> findAllByCarereceiverUserId(long userId);
}
