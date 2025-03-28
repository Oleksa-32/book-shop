package com.example.book.security;

public class SecurityContextHolder {
    private static final ThreadLocal<SecurityContext> securityContext = ThreadLocal
            .withInitial(SecurityContext::new);

    public static SecurityContext getSecurityContext() {
        return securityContext.get();
    }
}
