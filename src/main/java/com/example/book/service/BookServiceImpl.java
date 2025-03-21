package com.example.book.service;

import com.example.book.dto.BookDto;
import com.example.book.dto.BookSearchParametersDto;
import com.example.book.dto.CreateBookRequestDto;
import com.example.book.mapper.BookMapper;
import com.example.book.model.Book;
import com.example.book.repository.BookRepository;
import com.example.book.repository.book.BookSpecificationBuilder;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder specificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id " + id + " not found"));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto updateBook(Long id, BookDto updateRequest) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id " + id + " not found"));
        bookMapper.updateBookFromDto(updateRequest, existingBook);
        return bookMapper.toDto(bookRepository.save(existingBook));
    }

    @Override
    public void deleteBook(Long id) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id " + id + " not found"));
        existingBook.setDeleted(true);
        bookRepository.save(existingBook);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto searchParametersDto) {
        Specification<Book> bookSpecification = specificationBuilder.build(searchParametersDto);
        return bookRepository.findAll(bookSpecification).stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }
}
