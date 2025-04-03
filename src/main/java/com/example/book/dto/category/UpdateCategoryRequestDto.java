package com.example.book.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCategoryRequestDto {
    @NotBlank(message = "Category name is required")
    @Size(min = 3, max = 20, message = "Category name must be between 3 and 20")
    private String name;
    private String description;
}
