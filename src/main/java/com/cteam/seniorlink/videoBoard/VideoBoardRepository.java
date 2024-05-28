package com.cteam.seniorlink.videoBoard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoBoardRepository extends JpaRepository<VideoBoardEntity, Long> {
    Page<VideoBoardEntity> findByTitleContains(String title, Pageable pageable);
    Page<VideoBoardEntity>findAll(Pageable pageable);
}
