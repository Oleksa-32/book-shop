package com.example.book.security;

public class UsernamePasswordAuthenticationToken implements Authentication {

    private final String username;
    private final String password;

    public UsernamePasswordAuthenticationToken(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return password;
    }
}
