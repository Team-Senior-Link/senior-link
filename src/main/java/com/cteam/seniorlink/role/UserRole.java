package com.cteam.seniorlink.role;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    WORKER("ROLE_WORKER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
