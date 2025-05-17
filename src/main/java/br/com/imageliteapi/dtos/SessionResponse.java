package br.com.imageliteapi.dtos;

public record SessionResponse(
        boolean isAuthenticated,
        Long id,
        String email,
        String name,
        String profileImage
) {}
