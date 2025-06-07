package com.example.book.service.shoppingcart;

import com.example.book.dto.cartitem.CreateCartItemDto;
import com.example.book.dto.cartitem.UpdateCartItemDto;
import com.example.book.dto.shoppingcart.ShoppingCartDto;
import com.example.book.mapper.CartItemMapper;
import com.example.book.mapper.ShoppingCartMapper;
import com.example.book.model.Book;
import com.example.book.model.CartItem;
import com.example.book.model.ShoppingCart;
import com.example.book.model.User;
import com.example.book.repository.BookRepository;
import com.example.book.repository.CartItemRepository;
import com.example.book.repository.ShoppingCartRepository;
import com.example.book.repository.UserRepository;
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
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public ShoppingCartDto findByUserId(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Cart of user with id " + userId
                        + "wasn't found"));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto addItemToCart(CreateCartItemDto createCartItemDto, Long userId) {
        Book book = bookRepository.findById(createCartItemDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("can't find book with id "
                        + createCartItemDto.getBookId()));
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "can't find cart with userid: " + userId)
                );

        Long bookId = createCartItemDto.getBookId();
        Optional<CartItem> existingCartItem = cartItemRepository
                .findByBook_IdAndShoppingCart_Id(bookId, userId);

        if (existingCartItem.isEmpty()) {
            CartItem newCartItem = cartItemMapper.createToModel(createCartItemDto);
            newCartItem.setBook(book);
            newCartItem.setShoppingCart(cart);
            cart.getCartItems().add(newCartItem);
            cartItemRepository.save(newCartItem);
        } else {
            CartItem existingItem = existingCartItem.get();
            int newQuantity = existingItem.getQuantity() + createCartItemDto.getQuantity();
            existingItem.setQuantity(newQuantity);
            cartItemRepository.save(existingItem);
        }
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartDto update(Long cartItemId, UpdateCartItemDto updateCartItemDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find cart item by id "
                        + cartItemId));
        cartItemMapper.updateFromDto(updateCartItemDto, cartItem);
        cartItemRepository.save(cartItem);
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByCartItemsId(cartItemId).orElseThrow(
                        () -> new EntityNotFoundException("Cart of cart item with id " + cartItemId
                                + "wasn't found"));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
