package com.example.book.service;

import com.example.book.dto.book.BookDto;
import com.example.book.dto.book.BookDtoWithoutCategoryIds;
import com.example.book.dto.book.BookSearchParametersDto;
import com.example.book.dto.book.CreateBookRequestDto;
import com.example.book.dto.book.UpdateBookRequestDto;
import com.example.book.mapper.BookMapper;
import com.example.book.model.Book;
import com.example.book.repository.BookRepository;
import com.example.book.repository.CategoryRepository;
import com.example.book.repository.book.BookSpecificationBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder specificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        var book1 = bookRepository.save(book);
        return bookMapper.toDto(book1);
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id " + id + " not found"));
        return bookMapper.toDto(book);
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto updateBook(Long id, UpdateBookRequestDto updateRequest) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id " + id + " not found"));
        bookMapper.updateBookFromDto(updateRequest, existingBook);
        return bookMapper.toDto(bookRepository.save(existingBook));
    }

    @Override
    public Page<BookDtoWithoutCategoryIds> findByCategoryId(Long id, Pageable pageable) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category with id " + id + " not found");
        }
        return bookRepository
                .findByCategoryId(id, pageable)
                .map(bookMapper::toDtoWithoutCategories);
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with id " + id + " not found");
        }
        bookRepository.deleteById(id);
    }

    @Override
    public Page<BookDto> search(BookSearchParametersDto searchParametersDto,
                                Pageable pageable) {
        Specification<Book> bookSpecification = specificationBuilder.build(searchParametersDto);
        return bookRepository.findAll(bookSpecification, pageable)
                .map(bookMapper::toDto);
    }
}
