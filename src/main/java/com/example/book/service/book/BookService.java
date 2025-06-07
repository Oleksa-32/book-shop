package com.example.book.service.book;

import com.example.book.dto.book.BookDto;
import com.example.book.dto.book.BookDtoWithoutCategoryIds;
import com.example.book.dto.book.BookSearchParametersDto;
import com.example.book.dto.book.CreateBookRequestDto;
import com.example.book.dto.book.UpdateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getBookById(Long id);

    Page<BookDto> findAll(Pageable pageable);

    BookDto updateBook(Long id, UpdateBookRequestDto updateRequest);

    Page<BookDtoWithoutCategoryIds> findByCategoryId(Long id, Pageable pageable);

    BookDto findById(Long id);

    void deleteBook(Long id);

    Page<BookDto> search(BookSearchParametersDto searchParametersDto,
                         Pageable pageable);
}
