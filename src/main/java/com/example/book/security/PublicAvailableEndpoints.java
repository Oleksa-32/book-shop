package com.example.book.security;

import java.util.List;

public class PublicAvailableEndpoints {
    private static List<String> publicEndpoints = List.of(
            "/auth/login",
            "auth/registration"
    );

    public static List<String> getPublicEndpoints() {
        return publicEndpoints;
    }
}
