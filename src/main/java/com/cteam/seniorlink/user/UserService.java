package com.cteam.seniorlink.user;

import com.cteam.seniorlink.user.UserEntity;
import com.cteam.seniorlink.user.UserCreateRequest;
import com.cteam.seniorlink.user.UserDto;
import com.cteam.seniorlink.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    // 로그인한 user 이름 가져오기
    public UserDto getMember(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new UserDto().toDto(user);
    }

}
