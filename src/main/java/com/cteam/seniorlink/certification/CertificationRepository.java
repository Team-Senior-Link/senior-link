package com.cteam.seniorlink.certification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificationRepository extends JpaRepository<CertificationEntity, Long> {
    List<CertificationEntity> findByCaregiverNameContains(String caregiver);
}
