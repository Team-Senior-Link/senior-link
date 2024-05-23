package com.cteam.seniorlink.user.role;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    CAREGIVER("ROLE_CAREGIVER"),
    CARERECEIVER("ROLE_CARERECEIVER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
