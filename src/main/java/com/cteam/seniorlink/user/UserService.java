package com.cteam.seniorlink.user;

import com.cteam.seniorlink.schedule.ScheduleDto;
import com.cteam.seniorlink.schedule.ScheduleEntity;
import com.cteam.seniorlink.schedule.ScheduleRepository;
import com.cteam.seniorlink.user.role.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 정보 저장
    @Transactional
    public void saveUser(UserCreateRequest request) {
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword1()))
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .addressDetail(request.getAddressDetail())
                .role(request.getRole())
                .grade(request.getGrade())
                .status(false)
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

        // 요양보호사
        if (user.orElseThrow().getRole() == UserRole.CAREGIVER) {
            scheduleList = scheduleRepository.findAllByCaregiverUserId(userId);
        }
        // 시니어
        else if (user.orElseThrow().getRole() == UserRole.CARERECEIVER) {
            scheduleList = scheduleRepository.findAllByCarereceiverUserId(userId);
        }

        return scheduleList.stream()
                .map(ScheduleDto::toDto)
                .collect(Collectors.toList());
    }

    // 마이페이지 수정
    public UserEditRequest getUserEditRequest(String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    UserEditRequest userEditRequest = new UserEditRequest();
                    userEditRequest.setName(user.getName());
                    userEditRequest.setPhone(user.getPhone());
                    userEditRequest.setEmail(user.getEmail());
                    userEditRequest.setAddress(user.getAddress());
                    userEditRequest.setAddressDetail(user.getAddressDetail());
                    return userEditRequest;
                })
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public void editUser(String username, UserEditRequest userEditRequest, MultipartFile profileImgFile) throws IOException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        user.updateUser(userEditRequest);

        if (!profileImgFile.isEmpty()) {
            String profileImgPath = saveProfileImage(profileImgFile, user.getUserId());
            user.updateProfileImage(profileImgPath);
        }

        userRepository.save(user);
    }


    // 프로필 이미지 저장
    // 파일명 : [원본 이름_userId_현재시간]
    private String saveProfileImage(MultipartFile profileImgFile, Long userId) throws IOException {
        String originalFilename = profileImgFile.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String baseFilename = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String newFilename = baseFilename + "_" + userId + "_" + System.currentTimeMillis() + fileExtension;

        Path path = Paths.get(uploadDir).resolve(newFilename).toAbsolutePath().normalize();

        // 사진 파일을 저장할 디렉토리가 존재하지 않으면 생성
        if (Files.notExists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, profileImgFile.getBytes());

        return newFilename;
    }
}