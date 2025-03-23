package com.example.book.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PathValidator implements ConstraintValidator<Path, String> {
    private static final String PATH_PATTERN = "^(https?|ftp)://[^\s/$.?#].[^\s]*$";

    @Override
    public boolean isValid(String path, ConstraintValidatorContext constraintValidatorContext) {
        return path != null && Pattern.compile(PATH_PATTERN).matcher(path).matches();
    }
}
