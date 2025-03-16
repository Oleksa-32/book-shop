package com.example.book.repository;

import com.example.book.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List findAll();
}
