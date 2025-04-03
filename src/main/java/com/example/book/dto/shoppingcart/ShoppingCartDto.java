package com.example.book.dto.shoppingcart;

import com.example.book.dto.cartitem.CartItemDto;
import java.util.Set;

public record ShoppingCartDto(Long id, Long userId, Set<CartItemDto> cartItems) {
}
