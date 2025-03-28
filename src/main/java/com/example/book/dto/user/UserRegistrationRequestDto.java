package com.example.book.dto.user;

import com.example.book.validation.FieldMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatches(fields = {"password", "repeatPassword"}, message = "Password do not match")
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Length(min = 8, max = 20)
    private String password;
    @NotBlank
    @Length(min = 8, max = 20)
    private String repeatPassword;
}
