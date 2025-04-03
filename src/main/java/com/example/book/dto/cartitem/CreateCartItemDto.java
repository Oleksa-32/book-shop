package com.example.book.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateCartItemDto {
    @NotNull(message = "BookId is required")
    @Positive
    private Long bookId;
    @Positive(message = "Quantity can't be less than 0")
    private int quantity;
}
