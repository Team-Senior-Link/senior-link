package com.cteam.seniorlink.serviceBoard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    List<ServiceEntity> findByCaregiverNameContains(String caregiver);
    List<ServiceEntity> findByLocationContains(String location);

}
