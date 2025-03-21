package com.example.book.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PathValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface Path {
    String message() default "Invalid format path";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
