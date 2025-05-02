package com.example.book.service.user;

import com.example.book.dto.user.UserRegistrationRequestDto;
import com.example.book.dto.user.UserResponseDto;
import com.example.book.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
