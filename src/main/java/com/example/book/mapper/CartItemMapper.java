package com.example.book.mapper;

import com.example.book.config.MapperConfig;
import com.example.book.dto.cartitem.CartItemDto;
import com.example.book.dto.cartitem.CreateCartItemDto;
import com.example.book.dto.cartitem.UpdateCartItemDto;
import com.example.book.model.CartItem;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = {BookMapper.class})
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem createToModel(CreateCartItemDto createCartItemDto);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem dtoToModel(CartItemDto cartItemDto);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toModel(CreateCartItemDto cartItemDto);

    @Mapping(target = "id", ignore = true)
    void updateFromDto(UpdateCartItemDto updateCartItemDto, @MappingTarget CartItem cartItem);

    @Named("cartItemSetToModel")
    default Set<CartItem> cartItemSetToModel(Set<CartItemDto> cartItemDtoSet) {
        if (cartItemDtoSet == null) {
            return new HashSet<>();
        }
        return cartItemDtoSet.stream()
                .map(this::dtoToModel)
                .collect(Collectors.toSet());
    }
}
