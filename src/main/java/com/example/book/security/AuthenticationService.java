package com.example.book.security;

import com.example.book.dto.user.UserLoginRequestDto;
import com.example.book.model.User;
import com.example.book.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    public boolean authenticate(UserLoginRequestDto requestDto) {
        Optional<User> user = userRepository.findByEmail(requestDto.getEmail());
        return user.isPresent() && user.get().getPassword().equals(requestDto.getPassword());
    }
}
