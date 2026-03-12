package com.example.security7_practice.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    USER("USER"),
    ADMIN("ADMIN");

    private final String description;
}
