package com.conexia.service.dto;

public record LoggedUser(
        Long userId,
        String username,
        String role
) { }