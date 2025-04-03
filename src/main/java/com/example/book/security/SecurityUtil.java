package com.example.book.security;

import com.example.book.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Long getLoggedInUserId() {
        return ((User) getAuthentication().getPrincipal()).getId();
    }
}
