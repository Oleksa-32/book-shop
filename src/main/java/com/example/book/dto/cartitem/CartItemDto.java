package com.example.book.dto.cartitem;

public record CartItemDto(Long id, Long bookId,
                          String bookTitle, int quantity) {
}
