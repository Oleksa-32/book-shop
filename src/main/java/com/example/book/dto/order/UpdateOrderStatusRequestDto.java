package com.example.book.dto.order;

import com.example.book.model.Order;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDto {
    @NotBlank(message = "New order status is required")
    private Order.StatusName status;
}
