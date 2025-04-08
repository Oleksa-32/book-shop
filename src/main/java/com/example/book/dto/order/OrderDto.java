package com.example.book.dto.order;

import com.example.book.dto.orderitem.OrderItemDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderDto(Long id,
                        Set<OrderItemDto> orderItems,
                        LocalDateTime orderDate,
                        BigDecimal total,
                        String status) {
}
