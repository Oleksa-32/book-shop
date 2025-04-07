package com.example.book.dto.order;

import com.example.book.dto.orderitem.OrderItemDto;
import com.example.book.service.order.OrderService;
import com.example.book.service.shoppingcart.ShoppingCartService;
import com.example.book.validation.Path;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order Management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final ShoppingCartService shoppingCartService;
    private final OrderService orderService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create order",
            description = "Create new order with given date")
    public OrderDto createOrder(@Valid @RequestBody CreateOrderRequestDto requestDto) {
        return orderService.save(requestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(summary = "Get all user orders",
            description = "Get all user orders of currently authorized user")
    public List<OrderDto> getAllOrders(@ParameterObject Pageable pageable) {
        return orderService.getOrders(pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("{orderId}/items")
    @Operation(summary = "Get all items of user order",
            description = "Get all items for selected order "
                    + "of currently authorized user")
    public List<OrderItemDto> getAllOrders(@PathVariable Long orderId,
                                           @ParameterObject Pageable pageable) {
        return orderService.getOrderItems(orderId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("{orderId}/items/{itemId}")
    @Operation(summary = "Get item by id of user order",
            description = "Select item by id for selected order "
                    + "of currently authorized user")
    public OrderItemDto getOrderItem(@PathVariable Long orderId,
                                     @Path Long itemId) {
        return orderService.getOrderItemById(orderId, itemId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("{orderId}")
    @Operation(summary = "Change item status",
            description = "Change status for any selected order")
    public OrderDto updateOrderStatus(@PathVariable Long orderId,
                                      @Valid @RequestBody
                                      UpdateOrderStatusRequestDto orderStatusRequestDto) {
        return orderService.updateItemStatus(orderId, orderStatusRequestDto);
    }
}
