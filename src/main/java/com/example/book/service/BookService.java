package com.example.book.service;

import com.example.book.dto.BookDto;
import com.example.book.dto.CreateBookRequestDto;

import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    public BookDto getBookById(Long id);

    List<BookDto> findAll();
}
