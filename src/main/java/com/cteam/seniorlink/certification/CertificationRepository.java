package com.cteam.seniorlink.certification;

import com.cteam.seniorlink.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificationRepository extends JpaRepository<CertificationEntity, Long> {
    List<CertificationEntity> findByCaregiver(UserEntity caregiver); //caregiver로 검색
}
