package com.example.book.dto.order;

import com.example.book.model.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDto {
    @NotNull(message = "New order status is required")
    private Order.StatusName status;
}
