package com.example.book.service.order;

import com.example.book.dto.order.CreateOrderRequestDto;
import com.example.book.dto.order.OrderDto;
import com.example.book.dto.order.UpdateOrderStatusRequestDto;
import com.example.book.dto.orderitem.OrderItemDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    List<OrderDto> getOrders(Pageable pageable);

    List<OrderItemDto> getOrderItems(Long orderId, Pageable pageable);
    OrderItemDto getOrderItemById(Long orderId, Long itemId);

    OrderDto save(CreateOrderRequestDto orderRequestDto);

    OrderDto updateItemStatus(Long orderId,
                              UpdateOrderStatusRequestDto updateOrderStatusRequestDto);
}
