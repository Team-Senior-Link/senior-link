package com.cteam.seniorlink.repository.service;

import com.cteam.seniorlink.domain.service.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    List<ServiceEntity> findByCaregiverName(String Caregiver);
}
