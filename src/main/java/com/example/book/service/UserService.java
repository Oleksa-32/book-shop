package com.example.book.service;

import com.example.book.dto.user.UserRegistrationRequestDto;
import com.example.book.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);
}
