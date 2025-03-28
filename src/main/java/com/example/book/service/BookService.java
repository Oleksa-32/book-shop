package com.example.book.service;

import com.example.book.dto.BookDto;
import com.example.book.dto.BookSearchParametersDto;
import com.example.book.dto.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getBookById(Long id);

    Page<BookDto> findAll(Pageable pageable);

    BookDto updateBook(Long id, BookDto updateRequest);

    void deleteBook(Long id);

    Page<BookDto> search(BookSearchParametersDto searchParametersDto,
                         Pageable pageable);
}
