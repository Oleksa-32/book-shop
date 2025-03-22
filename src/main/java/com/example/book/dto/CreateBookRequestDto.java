package com.example.book.dto;

import com.example.book.validation.Path;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

@Data
public class CreateBookRequestDto {
    @NotNull
    @Size(max = 100, message = "Title length must be less then 100 characters")
    private String title;
    @NotNull
    private String author;
    @NotNull
    @ISBN
    private String isbn;
    @NotNull
    @Positive
    private BigDecimal price;
    private String description;
    @Path
    private String coverImage;
}
