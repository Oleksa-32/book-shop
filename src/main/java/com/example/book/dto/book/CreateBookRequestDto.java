package com.example.book.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.ISBN;

@Data
@Accessors(chain = true)
public class CreateBookRequestDto {
    @NotBlank
    @Size(max = 100, message = "Title length must be less then 100 characters")
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    @ISBN
    private String isbn;
    @NotNull
    @Positive
    private BigDecimal price;
    private String description;
    private String coverImage;
    private Set<Long> categoryIds;
}
