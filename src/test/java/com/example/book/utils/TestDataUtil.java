package com.example.book.utils;

import com.example.book.dto.book.BookDto;
import com.example.book.dto.book.BookDtoWithoutCategoryIds;
import com.example.book.dto.book.CreateBookRequestDto;
import com.example.book.dto.book.UpdateBookRequestDto;
import com.example.book.dto.category.CategoryDto;
import com.example.book.dto.category.CreateCategoryRequestDto;
import com.example.book.dto.category.UpdateCategoryRequestDto;
import com.example.book.model.Book;
import com.example.book.model.Category;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestDataUtil {
    public TestDataUtil() {
    }

    public static BookDto bookDto() {
        return new BookDto()
                .setId(2L)
                .setTitle("Book 2")
                .setAuthor("Author B")
                .setIsbn("9783161484117")
                .setPrice(BigDecimal.valueOf(25.99))
                .setCategoryIds(Set.of(2L));
    }

    public static Book book(List<Category> categories) {
        return new Book()
                .setId(3L)
                .setTitle("Book 2")
                .setAuthor("Author B")
                .setIsbn("1897629398234")
                .setPrice(BigDecimal.valueOf(25.99))
                .setCategories(Set.of(categories.get(0), categories.get(1)));
    }

    public static CreateBookRequestDto createBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("Title")
                .setAuthor("Author")
                .setIsbn("9783161484131")
                .setPrice(BigDecimal.valueOf(14.99))
                .setCategoryIds(Set.of(1L));
    }

    public static UpdateBookRequestDto updateBookRequestDto() {
        return new UpdateBookRequestDto()
                .setTitle("Updated Book Title")
                .setAuthor("Author B")
                .setIsbn("9783161484148")
                .setPrice(BigDecimal.valueOf(25.99))
                .setCategoryIds(Set.of(2L));
    }

    public static BookDto mapToBookDto(CreateBookRequestDto requestDto) {
        return new BookDto()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setCategoryIds(requestDto.getCategoryIds());
    }

    public static BookDto mapToBookDto(Long bookId, UpdateBookRequestDto requestDto) {
        return new BookDto()
                .setId(bookId)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setCategoryIds(requestDto.getCategoryIds());
    }

    public static BookDto mapToBookDto(Book book) {
        return new BookDto()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setCategoryIds(book.getCategories().stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet()));
    }

    public static Book mapToBook(CreateBookRequestDto createBookRequestDto,
                                 List<Category> categoryList) {
        return new Book()
                .setTitle(createBookRequestDto.getTitle())
                .setAuthor(createBookRequestDto.getAuthor())
                .setIsbn(createBookRequestDto.getIsbn())
                .setPrice(createBookRequestDto.getPrice())
                .setCategories(categoryList.stream()
                        .filter(c -> createBookRequestDto.getCategoryIds().contains(c.getId()))
                        .collect(Collectors.toSet()));
    }

    public static List<BookDto> bookDtoList() {
        List<BookDto> testList = new ArrayList<>();
        testList.add(new BookDto()
                .setId(1L)
                .setTitle("Book 1")
                .setAuthor("Author A")
                .setIsbn("9783161484100")
                .setPrice(BigDecimal.valueOf(20.99))
                .setCategoryIds(Set.of(1L))); // sci-fi
        testList.add(new BookDto()
                .setId(2L)
                .setTitle("Book 2")
                .setAuthor("Author B")
                .setIsbn("9783161484117")
                .setPrice(BigDecimal.valueOf(25.99))
                .setCategoryIds(Set.of(2L))); // fantasy
        testList.add(new BookDto()
                .setId(3L)
                .setTitle("Book 3")
                .setAuthor("Author C")
                .setIsbn("9783161484124")
                .setPrice(BigDecimal.valueOf(17.99))
                .setCategoryIds(Set.of(1L, 2L))); // sci-fi and fantasy
        return testList;
    }

    public static List<CategoryDto> categoryDtoList() {
        List<CategoryDto> testList = new ArrayList<>();
        testList.add(new CategoryDto().setId(3L)
                .setName("cooking"));
        testList.add(new CategoryDto().setId(4L)
                .setName("IT guides"));
        testList.add(new CategoryDto().setId(5L)
                .setName("novel"));
        testList.add(new CategoryDto().setId(6L)
                .setName("language"));
        return testList;
    }

    public static CreateCategoryRequestDto createCategoryRequestDto() {
        return new CreateCategoryRequestDto()
                .setName("language");
    }

    public static CategoryDto mapToCategoryDto(CreateCategoryRequestDto requestDto) {
        return new CategoryDto()
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription());
    }

    public static CategoryDto mapToCategoryDto(Long id, UpdateCategoryRequestDto requestDto) {
        return new CategoryDto()
                .setId(id)
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription());
    }

    public static CategoryDto mapToCategoryDto(Category category) {
        return new CategoryDto()
                .setId(category.getId())
                .setName(category.getName())
                .setDescription(category.getDescription());
    }

    public static UpdateCategoryRequestDto updateCategoryRequestDto() {
        return new UpdateCategoryRequestDto()
                .setName("Updated Category")
                .setDescription("Updated Description");
    }

    public static CategoryDto categoryDto() {
        return new CategoryDto()
                .setId(3L)
                .setName("cooking");
    }

    public static List<BookDtoWithoutCategoryIds> bookDtoWithoutCategoryIdsList() {
        List<BookDtoWithoutCategoryIds> expected = new ArrayList<>();
        expected.add(new BookDtoWithoutCategoryIds()
                .setId(5L)
                .setTitle("Sample Book 5")
                .setAuthor("Author B")
                .setIsbn("1236567163330")
                .setPrice(BigDecimal.valueOf(34.99)));
        expected.add(new BookDtoWithoutCategoryIds()
                .setId(6L)
                .setTitle("Sample Book 6")
                .setAuthor("Author C")
                .setIsbn("1236567163332")
                .setPrice(BigDecimal.valueOf(12.99)));
        return expected;
    }
}
