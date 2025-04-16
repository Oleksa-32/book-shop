package com.example.book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
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
import org.springframework.data.domain.Pageable;
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
    @DisplayName("Ensure that retrieving books with any Pageable argument returns the"
            + " correct BookDto list")
    public void retrieveAllBooks_returnsExpectedDtoPage() {
        Page<Book> bookPage = new PageImpl<>(bookList);

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);

        List<BookDto> expectedBookDtoList = bookList.stream()
                .map(bookMapper::toDto)
                .toList();

        Page<BookDto> actualList = bookService.findAll(PageRequest.of(0, 10));

        assertFalse(actualList.isEmpty());
        assertEquals(expectedBookDtoList.size(), actualList.getNumberOfElements());
        assertEquals(expectedBookDtoList, actualList.getContent());

        verify(bookRepository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Ensure that searching books with valid parameters and a Pageable"
            + " argument returns the expected BookDto list")
    public void searchBooks_withValidParameters_returnsExpectedDtoPage() {
        String searchBy = "s";

        List<Book> filteredByCharInNameList = bookList.stream()
                .filter(b -> b.getTitle().contains(searchBy))
                .toList();

        Page<Book> filteredBookPage = new PageImpl<>(filteredByCharInNameList);
        BookSearchParametersDto searchParametersDto = new BookSearchParametersDto(
                new String[]{searchBy}, new String[]{}, new String[]{});
        Specification<Book> bookSpecification = bookSpecificationBuilder
                .build(searchParametersDto);

        when(bookSpecificationBuilder.build(searchParametersDto)).thenReturn(bookSpecification);
        when(bookRepository.findAll(eq(bookSpecification), any(Pageable.class)))
                .thenReturn(filteredBookPage);
        List<BookDto> expectedFilterBookDto = filteredByCharInNameList.stream()
                .map(bookMapper::toDto)
                .toList();

        Page<BookDto> actualList = bookService.search(searchParametersDto, PageRequest.of(0, 10));

        assertFalse(actualList.isEmpty());
        assertEquals(expectedFilterBookDto.size(), actualList.getNumberOfElements());
        assertEquals(expectedFilterBookDto, actualList.getContent());

        verify(bookRepository, times(1)).findAll(eq(bookSpecification), any(Pageable.class));
        verify(bookSpecificationBuilder, times(2)).build(searchParametersDto);
    }

    @Test
    @DisplayName("Ensure that retrieving books by a valid category id returns the expected"
            + " BookDto list without category details")
    public void findBooksByCategory_returnsExpectedDtoWithoutCategoryIds() {
        Long categoryId = 1L;
        List<Book> filteredByCategoryId = bookList.stream()
                .filter(b -> b.getCategories().stream().anyMatch(c -> c.getId().equals(categoryId)))
                .toList();
        Page<Book> filteredBooksPage = new PageImpl<>(filteredByCategoryId);

        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(bookRepository.findByCategoryId(eq(categoryId), any(Pageable.class)))
                .thenReturn(filteredBooksPage);

        List<BookDtoWithoutCategoryIds> expectedFilterBookDto = filteredByCategoryId.stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();

        Page<BookDtoWithoutCategoryIds> actualList = bookService.findByCategoryId(categoryId,
                PageRequest.of(0, 10));

        assertFalse(actualList.isEmpty());
        assertEquals(expectedFilterBookDto.size(), actualList.getNumberOfElements());
        assertEquals(expectedFilterBookDto, actualList.getContent());

        verify(bookRepository, times(1)).findByCategoryId(eq(categoryId), any(Pageable.class));
        verify(categoryRepository, times(1)).existsById(categoryId);
    }

    @Test
    @DisplayName("Ensure that retrieving a book by a valid id returns the correct BookDto")
    public void retrieveBookById_returnsExpectedDto() {
        Long validBookId = 3L;
        Book book = TestDataUtil.book(categoryList);
        BookDto expectedBookDto = TestDataUtil.mapToBookDto(book);

        when(bookRepository.findById(validBookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedBookDto);

        BookDto actualbookDto = bookService.findById(validBookId);

        assertNotNull(actualbookDto);
        assertEquals(expectedBookDto, actualbookDto);

        verify(bookRepository, times(1)).findById(validBookId);
    }

    @Test
    @DisplayName("Expect an exception when retrieving a book with an invalid id")
    public void retrieveBookById_withInvalidId_throwsException() {
        Long invalidBookId = 5L;
        when(bookRepository.findById(invalidBookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(invalidBookId)
        );

        String expectedMessage = "Can't find book with id " + invalidBookId;
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        verify(bookRepository, times(1)).findById(invalidBookId);
    }

    @Test
    @DisplayName("Ensure that calling deleteBook with a valid id invokes"
            + " the repository's deleteById method")
    public void deleteBook_withValidId_callsRepositoryDelete() {
        Long validBookId = 1L;

        when(bookRepository.existsById(validBookId)).thenReturn(true);
        bookService.deleteBook(validBookId);

        verify(bookRepository).existsById(validBookId);
        verify(bookRepository, times(1)).deleteById(validBookId);
    }

    @Test
    @DisplayName("Ensure that updating a book with a valid id returns the updated BookDto")
    public void updateBook_withValidId_returnsUpdatedDto() {
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
        }).when(bookMapper).updateBookFromDto(eq(updateBookRequestDto), any(Book.class));
        when(bookRepository.save(bookToUpdate)).thenReturn(bookToUpdate);

        BookDto expectedBookDto = TestDataUtil.mapToBookDto(validBookId, updateBookRequestDto);

        when(bookMapper.toDto(bookToUpdate)).thenReturn(expectedBookDto);

        BookDto actualBookDto = bookService.updateBook(validBookId, updateBookRequestDto);

        assertNotNull(actualBookDto);
        assertEquals(expectedBookDto, actualBookDto);
        verify(bookRepository, times(1)).findById(validBookId);
        verify(bookMapper, times(1)).updateBookFromDto(eq(updateBookRequestDto), any(Book.class));
        verify(bookRepository, times(1)).save(bookToUpdate);
        verify(bookMapper, times(1)).toDto(bookToUpdate);
    }

    @Test
    @DisplayName("Expect an exception when attempting to update a book with an invalid id")
    public void updateBook_withInvalidId_throwsException() {
        Long invalidBookId = 5L;
        when(bookRepository.findById(invalidBookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.updateBook(invalidBookId, any(UpdateBookRequestDto.class))
        );

        String expectedMessage = "Book with id " + invalidBookId + " not found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        verify(bookRepository, times(1)).findById(invalidBookId);
    }

    @Test
    @DisplayName("Ensure that saving a book with a valid create request"
            + " returns the correct BookDto")
    public void createBook_withValidRequest_returnsExpectedDto() {
        CreateBookRequestDto createBookRequestDto = TestDataUtil.createBookRequestDto();
        Book book = TestDataUtil.mapToBook(createBookRequestDto, categoryList);
        BookDto expectedBookDto = TestDataUtil.mapToBookDto(book);

        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expectedBookDto);
        when(bookRepository.save(book)).thenReturn(book);

        BookDto actualBookDto = bookService.save(createBookRequestDto);

        assertNotNull(actualBookDto);
        assertEquals(expectedBookDto, actualBookDto);
        verify(bookMapper, times(1)).toModel(createBookRequestDto);
        verify(bookMapper, times(1)).toDto(book);
        verify(bookRepository, times(1)).save(book);
    }
}
