package com.cteam.seniorlink.schedule;

import com.cteam.seniorlink.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    List<ScheduleEntity> findByServiceId(long serviceId);
    List<ScheduleEntity> findByCarereceiver(UserEntity carereceiver);
}
