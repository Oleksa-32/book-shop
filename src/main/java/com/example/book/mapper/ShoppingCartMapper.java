package com.example.book.mapper;

import com.example.book.config.MapperConfig;
import com.example.book.dto.shoppingcart.ShoppingCartDto;
import com.example.book.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = {CartItemMapper.class})
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mapping(target = "cartItems", source = "cartItems",
            qualifiedByName = "cartItemSetToModel")
    @Mapping(target = "user.id", source = "userId")
    ShoppingCart toModel(ShoppingCartDto shoppingCartDto);

    void updateFromDto(ShoppingCartDto dto, @MappingTarget ShoppingCart cart);
}
