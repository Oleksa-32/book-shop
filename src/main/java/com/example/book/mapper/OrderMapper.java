package com.example.book.mapper;

import com.example.book.config.MapperConfig;
import com.example.book.dto.order.CreateOrderRequestDto;
import com.example.book.dto.order.OrderDto;
import com.example.book.model.CartItem;
import com.example.book.model.Order;
import com.example.book.model.ShoppingCart;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderDto toDto(Order order);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shippingAddress", source = "orderDto.shippingAddress")
    @Mapping(target = "total", source = "cart.cartItems",
            qualifiedByName = "getTotalPriceForOrder")
    Order toCreateReadyOrderFromCart(ShoppingCart cart, CreateOrderRequestDto orderRequestDto);

    @AfterMapping
    default void setDefaultStatus(@MappingTarget Order order) {
        order.setStatusName(Order.StatusName.PENDING);
    }

    @AfterMapping
    default void setCurrentTime(@MappingTarget Order order) {
        order.setOrderDate(LocalDateTime.now());
    }

    @Named("getTotalPriceForOrder")
    default BigDecimal getTotalPriceForOrder(Set<CartItem> cartItemSet) {
        return cartItemSet.stream()
                .map(c -> c.getBook().getPrice().multiply(
                        new BigDecimal(c.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
