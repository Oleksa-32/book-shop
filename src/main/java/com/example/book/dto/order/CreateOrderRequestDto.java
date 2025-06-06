package com.example.book.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    @NotBlank(message = "Shipping Address is required")
    private String shippingAddress;
}
