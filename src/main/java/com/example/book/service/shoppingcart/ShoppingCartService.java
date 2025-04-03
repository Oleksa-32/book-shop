package com.example.book.service.shoppingcart;

import com.example.book.dto.cartitem.CreateCartItemDto;
import com.example.book.dto.cartitem.UpdateCartItemDto;
import com.example.book.dto.shoppingcart.ShoppingCartDto;
import com.example.book.model.User;

public interface ShoppingCartService {
    ShoppingCartDto findByUserId(Long userId);

    ShoppingCartDto findByCartItemsId(Long cartItemId);

    ShoppingCartDto addItemToCart(CreateCartItemDto createCartItemDto, Long userId);

    ShoppingCartDto update(Long cartItemId, UpdateCartItemDto updateCartItemDto);

    void deleteById(Long id);

    void createShoppingCartForUser(User user);
}
