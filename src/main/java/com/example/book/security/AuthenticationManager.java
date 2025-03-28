package com.example.book.security;

import com.example.book.dto.user.UserLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationManager {
    private final AuthenticationService authenticationService;

    public boolean isAuthenticated(Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        String email = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        return authenticationService.authenticate(new UserLoginRequestDto(email, password));
    }
}
