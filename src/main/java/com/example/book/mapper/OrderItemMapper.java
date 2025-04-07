package com.example.book.mapper;

import com.example.book.config.MapperConfig;
import com.example.book.dto.orderitem.OrderItemDto;
import com.example.book.model.CartItem;
import com.example.book.model.Order;
import com.example.book.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {BookMapper.class})
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "item.book.price", target = "price")
    @Mapping(source = "item.deleted", target = "deleted")
    OrderItem toOrderItemFromCartItem(CartItem cartItem, Order order);
}
