package com.cteam.seniorlink.user;

import com.cteam.seniorlink.user.role.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, length = 500)
    private String username;

    @Column(length = 500)
    private String password;

    @Column(length = 500)
    private String name;

    @Column(length = 50)
    private String phone;

    @Column(unique = true, length = 500)
    private String email;

    @Column(length = 1000)
    private String address;

    @Column(length = 1000)
    private String addressDetail;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = true)
    private Integer grade;

    @Column
    private boolean status;

    @Column(length = 500)
    private String profileImgPath;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    // 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role.getValue()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 패스워드 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 사용 가능 여부
    @Override
    public boolean isEnabled() {
        return true;
    }

    public void updateUser(UserEditRequest request) {
        this.name = request.getName();
        this.phone = request.getPhone();
        this.email = request.getEmail();
        this.address = request.getAddress();
        this.addressDetail = request.getAddressDetail();
    }

    public void updateProfileImage(String profileImgPath) {
        this.profileImgPath = profileImgPath;
    }
}
