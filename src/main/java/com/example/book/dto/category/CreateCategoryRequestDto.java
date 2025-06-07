package com.example.book.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateCategoryRequestDto {
    @NotBlank
    @Size(min = 2, max = 100, message = "Name length must be at lest 4 characters"
            + " and not longer then 100 characters")
    private String name;
    private String description;
}
