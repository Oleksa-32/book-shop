package com.example.book.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@Getter
public class UserLoginRequestDto {
    @NotEmpty
    @Length(min = 8, max = 20)
    @Email
    private String email;
    @NotEmpty
    @Length(min = 8, max = 20)
    private String password;
}
