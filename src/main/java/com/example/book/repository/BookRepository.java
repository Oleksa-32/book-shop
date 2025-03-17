package com.example.book.repository;

import com.example.book.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);

    Optional<Book> findByID(Long id);

    List<Book> findAll();
}
