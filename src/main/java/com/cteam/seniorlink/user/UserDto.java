package com.cteam.seniorlink.user;

import com.cteam.seniorlink.user.role.UserRole;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long userId;

    private String username;

    private String password;

    private String name;

    private String phone;

    private String email;

    private String address;

    private String role;

    private int grade;

    private boolean status;

    private String profileImgPath;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private MultipartFile f;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .userId(this.userId)
                .username(this.username)
                .password(this.password)
                .name(this.name)
                .phone(this.phone)
                .email(this.email)
                .address(this.address)
                .role(this.role)
                .grade(this.grade)
                .status(this.status)
                .profileImgPath(this.profileImgPath)
                .build();
    }

    public UserDto toDto(UserEntity u){
        return UserDto.builder()
                .userId(u.getUserId())
                .username(u.getUsername())
                .password(u.getPassword())
                .name(u.getName())
                .phone(u.getPhone())
                .email(u.getEmail())
                .address(u.getAddress())
                .role(u.getRole())
                .grade(u.getGrade())
                .status(u.isStatus())
                .profileImgPath(u.getProfileImgPath())
                .createdAt(u.getCreatedAt())
                .updatedAt(u.getUpdatedAt())
                .f(null)
                .build();



    }

}
