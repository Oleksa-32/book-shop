package com.example.book.repository;

import com.example.book.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @EntityGraph(attributePaths = {"book", "cart", "cart.user"})
    Optional<CartItem> findByBook_IdAndShoppingCart_Id(Long bookId, Long cartId);

    @EntityGraph(attributePaths = {"book", "cart"})
    Optional<CartItem> findById(Long id);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.shoppingCart.id = :cartId")
    void deleteAllByCartId(Long cartId);

}
