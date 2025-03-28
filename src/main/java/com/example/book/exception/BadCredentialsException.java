package com.example.book.exception;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException(String message) {
        super(message);
    }
}
