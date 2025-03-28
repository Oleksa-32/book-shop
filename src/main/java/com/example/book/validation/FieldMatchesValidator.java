package com.example.book.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Objects;

public class FieldMatchesValidator implements ConstraintValidator<FieldMatches, Object> {
    private String[] fieldNames;

    @Override
    public void initialize(FieldMatches constraintAnnotation) {
        this.fieldNames = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            Field firstField = obj.getClass().getDeclaredField(fieldNames[0]);
            firstField.setAccessible(true);
            Object firstValue = firstField.get(obj);

            for (int i = 1; i < fieldNames.length; i++) {
                Field currentField = obj.getClass().getDeclaredField(fieldNames[i]);
                currentField.setAccessible(true);

                if (!Objects.equals(firstValue, currentField.get(obj))) {
                    return false;
                }
            }
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}
