package com.example.book.service;

import com.example.book.dto.book.BookDto;
import com.example.book.dto.book.BookDtoWithoutCategoryIds;
import com.example.book.dto.book.BookSearchParametersDto;
import com.example.book.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getBookById(Long id);

    Page<BookDto> findAll(Pageable pageable);

    BookDto updateBook(Long id, BookDto updateRequest);

    List<BookDtoWithoutCategoryIds> findByCategoryId(Long id, Pageable pageable);

    void deleteBook(Long id);

    Page<BookDto> search(BookSearchParametersDto searchParametersDto,
                         Pageable pageable);
}
