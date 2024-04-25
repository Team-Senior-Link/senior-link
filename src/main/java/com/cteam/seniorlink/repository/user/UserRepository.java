package com.cteam.seniorlink.repository.user;

import com.cteam.seniorlink.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
