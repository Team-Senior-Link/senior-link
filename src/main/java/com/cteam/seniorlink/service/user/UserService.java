package com.cteam.seniorlink.service.user;

import com.cteam.seniorlink.domain.user.UserEntity;
import com.cteam.seniorlink.dto.user.UserCreateRequest;
import com.cteam.seniorlink.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void saveUser(UserCreateRequest request) {
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword1()))
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .role(request.getRole())
                .status(false)
                .profileImgPath(request.getProfileImgPath())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }
}
