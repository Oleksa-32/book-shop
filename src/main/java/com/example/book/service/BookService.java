package com.example.book.service;

import com.example.book.dto.BookDto;
import com.example.book.dto.BookSearchParametersDto;
import com.example.book.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getBookById(Long id);

    List<BookDto> findAll();

    BookDto updateBook(Long id, BookDto updateRequest);

    void deleteBook(Long id);

    List<BookDto> search(BookSearchParametersDto searchParametersDto);
}
