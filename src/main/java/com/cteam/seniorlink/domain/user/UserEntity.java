package com.cteam.seniorlink.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class UserEntity {

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

    @Column
    private String role;

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
}
