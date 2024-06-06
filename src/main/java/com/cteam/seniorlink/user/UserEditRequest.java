package com.cteam.seniorlink.user;

import jakarta.validation.constraints.Email;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEditRequest {
    private String name;

    private String phone;

    @Email
    private String email;

    private String address;

    private String addressDetail;
}