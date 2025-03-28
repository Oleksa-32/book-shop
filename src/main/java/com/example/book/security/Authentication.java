package com.example.book.security;

public interface Authentication {
    Object getPrincipal();

    Object getCredentials();
}
