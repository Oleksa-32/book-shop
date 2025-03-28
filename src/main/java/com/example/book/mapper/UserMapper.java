package com.example.book.mapper;

import com.example.book.config.MapperConfig;
import com.example.book.dto.user.UserRegistrationRequestDto;
import com.example.book.dto.user.UserResponseDto;
import com.example.book.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto userRegistrationRequestDto);
}
