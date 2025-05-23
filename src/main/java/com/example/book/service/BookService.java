package com.example.book.service;

import com.example.book.dto.book.BookDto;
import com.example.book.dto.book.BookDtoWithoutCategoryIds;
import com.example.book.dto.book.BookSearchParametersDto;
import com.example.book.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getBookById(Long id);

    Page<BookDto> findAll(Pageable pageable);

    BookDto updateBook(Long id, BookDto updateRequest);

    Page<BookDtoWithoutCategoryIds> findByCategoryId(Long id, Pageable pageable);

    BookDto findById(Long id);

    void deleteBook(Long id);

    Page<BookDto> search(BookSearchParametersDto searchParametersDto,
                         Pageable pageable);
}
