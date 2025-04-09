package com.example.book.service.order;

import com.example.book.dto.order.CreateOrderRequestDto;
import com.example.book.dto.order.OrderDto;
import com.example.book.dto.order.UpdateOrderStatusRequestDto;
import com.example.book.dto.orderitem.OrderItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderDto> getOrders(Pageable pageable);

    Page<OrderItemDto> getOrderItems(Long orderId, Pageable pageable);

    OrderItemDto getOrderItemById(Long orderId, Long itemId);

    OrderDto save(CreateOrderRequestDto orderRequestDto);

    OrderDto updateItemStatus(Long orderId,
                              UpdateOrderStatusRequestDto updateOrderStatusRequestDto);
}
