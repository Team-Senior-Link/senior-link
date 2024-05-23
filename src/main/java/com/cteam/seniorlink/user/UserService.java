package com.cteam.seniorlink.user;

import com.cteam.seniorlink.schedule.ScheduleDto;
import com.cteam.seniorlink.schedule.ScheduleEntity;
import com.cteam.seniorlink.schedule.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
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
                .grade(request.getGrade())
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

    // 마이페이지
    public List<ScheduleDto> getMypageScheduleByUsername(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        Long userId = user.orElseThrow().getUserId();
        List<ScheduleEntity> scheduleList = new ArrayList<>();

        // String(role)
        // 요양보호사
        if ("ROLE_CAREGIVER".equals(user.get().getRole())) {
            scheduleList = scheduleRepository.findAllByCaregiverUserId(userId);
        }
        // 시니어
        else if ("ROLE_CARERECEIVER".equals(user.get().getRole())) {
            scheduleList = scheduleRepository.findAllByCarereceiverUserId(userId);
        }

        //        // Enum(user_role)
//        // 요양보호사
//        if (user.orElseThrow().getUserRole() == UserRole.CAREGIVER) {
//            scheduleList = scheduleRepository.findAllByCaregiverUserId(userId);
//        }
//        // 시니어
//        else if (user.orElseThrow().getUserRole() == UserRole.CARERECEIVER) {
//            scheduleList = scheduleRepository.findAllByCarereceiverUserId(userId);
//        }

        return scheduleList.stream()
                .map(ScheduleDto::toDto)
                .collect(Collectors.toList());
    }

}
