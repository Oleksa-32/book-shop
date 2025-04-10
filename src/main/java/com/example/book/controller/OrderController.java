package com.example.book.controller;

import com.example.book.dto.order.CreateOrderRequestDto;
import com.example.book.dto.order.OrderDto;
import com.example.book.dto.order.UpdateOrderStatusRequestDto;
import com.example.book.dto.orderitem.OrderItemDto;
import com.example.book.service.order.OrderService;
import com.example.book.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order Management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final ShoppingCartService cartService;
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create order",
            description = "Create new order with given date")
    public OrderDto createOrder(@Valid @RequestBody CreateOrderRequestDto requestDto) {
        return orderService.save(requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get all user orders",
            description = "Get all user orders of currently authorized user")
    public Page<OrderDto> getAllOrders(@ParameterObject Pageable pageable) {
        return orderService.getOrders(pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{orderId}/items")
    @Operation(summary = "Get all items of user order",
            description = "Get all items for selected order "
                    + "of currently authorized user")
    public Page<OrderItemDto> getAllOrders(@PathVariable Long orderId,
                                           @ParameterObject Pageable pageable) {
        return orderService.getOrderItems(orderId, pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{orderId}/items/{itemId}")
    @Operation(summary = "Get item by id of user order",
            description = "Select item by id for selected order "
                    + "of currently authorized user")
    public OrderItemDto getOrderItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.getOrderItemById(orderId, itemId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{orderId}")
    @Operation(summary = "Change item status",
            description = "Change status for any selected order")
    public OrderDto updateOrderStatus(@PathVariable Long orderId,
                                      @Valid @RequestBody UpdateOrderStatusRequestDto
                                              orderStatusRequestDto) {
        return orderService.updateItemStatus(orderId, orderStatusRequestDto);
    }
}
