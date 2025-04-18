package com.example.book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.book.dto.book.BookDto;
import com.example.book.dto.book.BookDtoWithoutCategoryIds;
import com.example.book.dto.book.BookSearchParametersDto;
import com.example.book.dto.book.CreateBookRequestDto;
import com.example.book.dto.book.UpdateBookRequestDto;
import com.example.book.mapper.BookMapper;
import com.example.book.model.Book;
import com.example.book.model.Category;
import com.example.book.repository.BookRepository;
import com.example.book.repository.CategoryRepository;
import com.example.book.repository.book.BookSpecificationBuilder;
import com.example.book.utils.TestDataUtil;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;

    private List<Book> bookList;
    private List<Category> categoryList;

    @BeforeEach
    void setUp() {
        Category sciFiCategory = new Category()
                .setId(1L)
                .setName("Science Fiction");

        Category fantasyCategory = new Category()
                .setId(2L)
                .setName("Fantasy");

        Book firstBook = new Book().setId(1L)
                .setTitle("Dune")
                .setAuthor("Frank Herbert")
                .setIsbn("1234567890123")
                .setPrice(BigDecimal.valueOf(15))
                .setCategories(Set.of(sciFiCategory));

        Book secondBook = new Book()
                .setId(2L)
                .setTitle("Game of Thrones")
                .setAuthor("George Martyn")
                .setIsbn("1234567890124")
                .setPrice(BigDecimal.valueOf(20))
                .setCategories(Set.of(fantasyCategory));

        Book thirdBook = new Book()
                .setId(3L)
                .setTitle("Star Wars")
                .setAuthor("Timothy Zan")
                .setIsbn("1234567890125")
                .setPrice(BigDecimal.valueOf(25))
                .setCategories((Set.of(sciFiCategory, fantasyCategory)));

        bookList = List.of(firstBook, secondBook, thirdBook);
        categoryList = List.of(fantasyCategory, sciFiCategory);
    }

    @Test
    @DisplayName("retrieveAllBooks returns expected Page<BookDto>")
    void retrieveAllBooks_returnsExpectedDtoPage() {
        Page<Book> bookPage = new PageImpl<>(bookList);
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(bookRepository.findAll(pageRequest)).thenReturn(bookPage);

        List<BookDto> expectedBookDtoList = bookList.stream()
                .map(bookMapper::toDto)
                .toList();

        Page<BookDto> actualList = bookService.findAll(pageRequest);

        assertFalse(actualList.isEmpty());
        assertEquals(expectedBookDtoList.size(), actualList.getNumberOfElements());
        assertEquals(expectedBookDtoList, actualList.getContent());

        verify(bookRepository).findAll(pageRequest);
    }

    @Test
    @DisplayName("searchBooks with valid parameters returns expected Page<BookDto>")
    void searchBooks_withValidParameters_returnsExpectedDtoPage() {
        String searchBy = "s";
        List<Book> filteredByCharInNameList = bookList.stream()
                .filter(b -> b.getTitle().contains(searchBy))
                .toList();
        Page<Book> filteredBookPage = new PageImpl<>(filteredByCharInNameList);
        PageRequest pageRequest = PageRequest.of(0, 10);

        BookSearchParametersDto searchParametersDto = new BookSearchParametersDto(
                new String[]{searchBy}, new String[]{}, new String[]{}
        );
        Specification<Book> bookSpecification = mock(Specification.class);

        when(bookSpecificationBuilder.build(searchParametersDto)).thenReturn(bookSpecification);
        when(bookRepository.findAll(bookSpecification, pageRequest))
                .thenReturn(filteredBookPage);

        List<BookDto> expectedFilterBookDto = filteredByCharInNameList.stream()
                .map(bookMapper::toDto)
                .toList();

        Page<BookDto> actualList = bookService.search(searchParametersDto, pageRequest);

        assertFalse(actualList.isEmpty());
        assertEquals(expectedFilterBookDto.size(), actualList.getNumberOfElements());
        assertEquals(expectedFilterBookDto, actualList.getContent());

        verify(bookSpecificationBuilder).build(searchParametersDto);
        verify(bookRepository).findAll(bookSpecification, pageRequest);
    }

    @Test
    @DisplayName("findByCategoryId returns expected Page<BookDtoWithoutCategoryIds>")
    void findBooksByCategory_returnsExpectedDtoWithoutCategoryIds() {
        Long categoryId = 1L;
        List<Book> filteredByCategoryId = bookList.stream()
                .filter(b -> b.getCategories().stream().anyMatch(c -> c.getId().equals(categoryId)))
                .toList();
        Page<Book> filteredBooksPage = new PageImpl<>(filteredByCategoryId);
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(bookRepository.findByCategoryId(categoryId, pageRequest))
                .thenReturn(filteredBooksPage);

        List<BookDtoWithoutCategoryIds> expectedFilterBookDto = filteredByCategoryId.stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();

        Page<BookDtoWithoutCategoryIds> actualList = bookService
                .findByCategoryId(categoryId, pageRequest);

        assertFalse(actualList.isEmpty());
        assertEquals(expectedFilterBookDto.size(), actualList.getNumberOfElements());
        assertEquals(expectedFilterBookDto, actualList.getContent());

        verify(categoryRepository).existsById(categoryId);
        verify(bookRepository).findByCategoryId(categoryId, pageRequest);
    }

    @Test
    @DisplayName("retrieveBookById returns expected BookDto")
    void retrieveBookById_returnsExpectedDto() {
        Long validBookId = 3L;
        Book book = TestDataUtil.book(categoryList);
        BookDto expectedBookDto = TestDataUtil.mapToBookDto(book);

        when(bookRepository.findById(validBookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedBookDto);

        BookDto actualbookDto = bookService.findById(validBookId);

        assertNotNull(actualbookDto);
        assertEquals(expectedBookDto, actualbookDto);

        verify(bookRepository).findById(validBookId);
    }

    @Test
    @DisplayName("retrieveBookById with invalid id throws exception")
    void retrieveBookById_withInvalidId_throwsException() {
        Long invalidBookId = 5L;
        UpdateBookRequestDto updateBookRequestDto = TestDataUtil.updateBookRequestDto();
        when(bookRepository.findById(invalidBookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(invalidBookId)
        );

        assertEquals("Can't find book with id " + invalidBookId, exception.getMessage());
        verify(bookRepository).findById(invalidBookId);
    }

    @Test
    @DisplayName("deleteBook with valid id invokes deleteById")
    void deleteBook_withValidId_callsRepositoryDelete() {
        Long validBookId = 1L;
        when(bookRepository.existsById(validBookId)).thenReturn(true);

        bookService.deleteBook(validBookId);

        verify(bookRepository).existsById(validBookId);
        verify(bookRepository).deleteById(validBookId);
    }

    @Test
    @DisplayName("updateBook with valid id returns updated BookDto")
    void updateBook_withValidId_returnsUpdatedDto() {
        Long validBookId = 2L;
        UpdateBookRequestDto updateBookRequestDto = TestDataUtil.updateBookRequestDto();

        Book bookToUpdate = bookList.stream()
                .filter(b -> b.getId().equals(validBookId))
                .findFirst()
                .orElseThrow();

        when(bookRepository.findById(validBookId)).thenReturn(Optional.of(bookToUpdate));
        doAnswer(invocation -> {
            Book bookArg = invocation.getArgument(1);
            bookArg.setTitle(updateBookRequestDto.getTitle());
            bookArg.setAuthor(updateBookRequestDto.getAuthor());
            bookArg.setIsbn(updateBookRequestDto.getIsbn());
            bookArg.setPrice(updateBookRequestDto.getPrice());
            bookArg.setCategories(categoryList.stream()
                    .filter(c -> updateBookRequestDto.getCategoryIds().contains(c.getId()))
                    .collect(Collectors.toSet()));
            return null;
        }).when(bookMapper).updateBookFromDto(updateBookRequestDto, bookToUpdate);

        when(bookRepository.save(bookToUpdate)).thenReturn(bookToUpdate);
        BookDto expectedBookDto = TestDataUtil.mapToBookDto(validBookId, updateBookRequestDto);
        when(bookMapper.toDto(bookToUpdate)).thenReturn(expectedBookDto);

        BookDto actualBookDto = bookService.updateBook(validBookId, updateBookRequestDto);

        assertNotNull(actualBookDto);
        assertEquals(expectedBookDto, actualBookDto);

        verify(bookRepository).findById(validBookId);
        verify(bookMapper).updateBookFromDto(updateBookRequestDto, bookToUpdate);
        verify(bookRepository).save(bookToUpdate);
        verify(bookMapper).toDto(bookToUpdate);
    }

    @Test
    @DisplayName("updateBook with invalid id throws exception")
    void updateBook_withInvalidId_throwsException() {
        Long invalidBookId = 5L;
        UpdateBookRequestDto updateBookRequestDto = TestDataUtil.updateBookRequestDto();
        when(bookRepository.findById(invalidBookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.updateBook(invalidBookId, updateBookRequestDto)
        );

        assertEquals("Book with id " + invalidBookId + " not found", exception.getMessage());
        verify(bookRepository).findById(invalidBookId);
    }

    @Test
    @DisplayName("save with valid request returns expected BookDto")
    void createBook_withValidRequest_returnsExpectedDto() {
        CreateBookRequestDto createBookRequestDto = TestDataUtil.createBookRequestDto();
        Book book = TestDataUtil.mapToBook(createBookRequestDto, categoryList);
        BookDto expectedBookDto = TestDataUtil.mapToBookDto(book);

        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expectedBookDto);

        BookDto actualBookDto = bookService.save(createBookRequestDto);

        assertNotNull(actualBookDto);
        assertEquals(expectedBookDto, actualBookDto);

        verify(bookMapper).toModel(createBookRequestDto);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }
}
