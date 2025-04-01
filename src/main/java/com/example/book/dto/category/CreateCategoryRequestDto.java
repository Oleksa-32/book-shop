package com.example.book.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    @NotBlank
    @Size(max = 100, message = "Name length must be less then 100 characters")
    private String name;
    private String description;
}
