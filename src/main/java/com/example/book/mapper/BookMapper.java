package com.example.book.mapper;

import com.example.book.config.MapperConfig;
import com.example.book.dto.book.BookDto;
import com.example.book.dto.book.BookDtoWithoutCategoryIds;
import com.example.book.dto.book.CreateBookRequestDto;
import com.example.book.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = {CategoryMapper.class})
public interface BookMapper {
    @Mapping(target = "id", ignore = true)
    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    @Mapping(target = "categories", source = "categoryIds", qualifiedByName = "categoriesByIds")
    Book dtoToEntity(BookDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateBookFromDto(BookDto bookDto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @Named("bookFromId")
    default Book bookFromId(Long bookId) {
        if (bookId == null) {
            return null;
        }
        Book book = new Book();
        book.setId(bookId);
        return book;
    }
}
