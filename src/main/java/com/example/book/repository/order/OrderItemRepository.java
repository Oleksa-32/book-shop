package com.example.book.repository.order;

import com.example.book.model.OrderItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @EntityGraph(attributePaths = "order.user")
    Optional<OrderItem> findByIdAndOrderIdAndOrderUserId(Long id, Long order, Long userId);

    @EntityGraph(attributePaths = "order.user")
    Page<OrderItem> findAllByOrderIdAndOrderUserId(Long orderId, Long userId, Pageable pageable);
}
