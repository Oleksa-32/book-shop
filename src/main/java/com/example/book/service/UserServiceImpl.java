package com.example.book.service;

import com.example.book.dto.user.UserRegistrationRequestDto;
import com.example.book.dto.user.UserResponseDto;
import com.example.book.exception.RegistrationException;
import com.example.book.mapper.UserMapper;
import com.example.book.model.User;
import com.example.book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can't register user");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(requestDto.getPassword());
        return userMapper.toDto(userRepository.save(user));
    }
}
