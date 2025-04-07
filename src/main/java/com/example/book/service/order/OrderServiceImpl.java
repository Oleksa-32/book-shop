package com.example.book.service.order;

import com.example.book.dto.order.CreateOrderRequestDto;
import com.example.book.dto.order.OrderDto;
import com.example.book.dto.order.UpdateOrderStatusRequestDto;
import com.example.book.dto.orderitem.OrderItemDto;
import com.example.book.exception.OrderProcessingException;
import com.example.book.mapper.OrderItemMapper;
import com.example.book.mapper.OrderMapper;
import com.example.book.model.CartItem;
import com.example.book.model.Order;
import com.example.book.model.OrderItem;
import com.example.book.model.ShoppingCart;
import com.example.book.repository.CartItemRepository;
import com.example.book.repository.ShoppingCartRepository;
import com.example.book.repository.order.OrderItemRepository;
import com.example.book.repository.order.OrderRepository;
import com.example.book.security.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartItemRepository cartItemRepository;
    @Override
    public List<OrderDto> getOrders(Pageable pageable) {
        Long userId = SecurityUtil.getLoggedInUserId();
        return orderRepository.findAllByUserById(userId, pageable)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long orderId, Pageable pageable) {
        Long userId = SecurityUtil.getLoggedInUserId();
        List<OrderItem> orderItems = orderItemRepository
                .findAllByOrderIdAndOrderUserId(orderId, userId, pageable);
        return orderItems.stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long itemId) {
        Long userId = SecurityUtil.getLoggedInUserId();
        OrderItem orderItem = orderItemRepository
                .findByIdAndOrderIdAndOrderUserId(itemId, orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Item with id + "
                        + itemId + "in order " + orderId + "for current user wasn't found"));
        return orderItemMapper.toDto(orderItem);
    }

    @Override
    public OrderDto save(CreateOrderRequestDto orderRequestDto) {
        Long userId = SecurityUtil.getLoggedInUserId();
        ShoppingCart shoppingCart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart for user"
                        + "with id" + "userId" + " not found"));
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("Cart is empty. Add some item first");
        }
        Order order = orderMapper.toCreateReadyOrderFromCart(shoppingCart, orderRequestDto);
        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map((CartItem item) ->
                        orderItemMapper.toOrderItemFromCartItem(item, order))
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        orderRepository.save(order);
        cartItemRepository.deleteAllByCartId(shoppingCart.getId());
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto updateItemStatus(Long orderId, UpdateOrderStatusRequestDto updateOrderStatusRequestDto) {
        Long userId = SecurityUtil.getLoggedInUserId();
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order "
                        + orderId + " wasn't found"));
        return orderMapper.toDto(orderRepository.save(order));
    }
}
