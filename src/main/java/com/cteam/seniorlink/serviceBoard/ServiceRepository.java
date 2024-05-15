package com.cteam.seniorlink.serviceBoard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    Page<ServiceEntity> findByCaregiverNameContains(String caregiver, Pageable pageable); //요양보호사로 검색

    Page<ServiceEntity> findByLocationContains(String location, Pageable pageable); //활동지역으로 검색

    Page<ServiceEntity> findAll(Pageable pageable); //페이징
}
