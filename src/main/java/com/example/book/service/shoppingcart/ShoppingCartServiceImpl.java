package com.example.book.service.shoppingcart;

import com.example.book.dto.cartitem.CreateCartItemDto;
import com.example.book.dto.cartitem.UpdateCartItemDto;
import com.example.book.dto.shoppingcart.ShoppingCartDto;
import com.example.book.mapper.BookMapper;
import com.example.book.mapper.CartItemMapper;
import com.example.book.mapper.ShoppingCartMapper;
import com.example.book.model.CartItem;
import com.example.book.model.ShoppingCart;
import com.example.book.model.User;
import com.example.book.repository.CartItemRepository;
import com.example.book.repository.ShoppingCartRepository;
import com.example.book.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookService bookService;
    private final BookMapper bookMapper;

    @Override
    public ShoppingCartDto findByUserId(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Cart of user with id " + userId
                        + "wasn't found"));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto findByCartItemsId(Long cartItemId) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByCartItemsId(cartItemId).orElseThrow(
                        () -> new EntityNotFoundException("Cart of cart item with id " + cartItemId
                                + "wasn't found"));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto addItemToCart(CreateCartItemDto createCartItemDto, Long userId) {
        ShoppingCartDto cart = findByUserId(userId);
        Long bookId = createCartItemDto.getBookId();
        Optional<CartItem> cartItem = cartItemRepository
                .findByBook_IdAndShoppingCart_Id(bookId, userId);
        if (cartItem.isEmpty()) {
            CartItem item = cartItemMapper.createToModel((createCartItemDto));
            item.setBook(bookMapper.dtoToEntity(bookService.findById(bookId)));
            item.setShoppingCart(shoppingCartMapper.toModel(cart));
            cartItemRepository.save(item);
            cart.cartItems().add(cartItemMapper.toDto(item));
            return cart;
        } else {
            CartItem existItem = cartItem.get();
            cartItemMapper.updateFromDto(
                    new UpdateCartItemDto(createCartItemDto.getQuantity()), existItem);
            cartItemRepository.save(existItem);
            return shoppingCartMapper.toDto(existItem.getShoppingCart());
        }
    }

    @Override
    public ShoppingCartDto update(Long cartItemId, UpdateCartItemDto updateCartItemDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find cart item by id "
                        + cartItemId));
        cartItemMapper.updateFromDto(updateCartItemDto, cartItem);
        cartItemRepository.save(cartItem);
        return findByCartItemsId(cartItem.getId());
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public void createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
