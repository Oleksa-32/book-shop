package com.example.book.mapper;

import com.example.book.config.MapperConfig;
import com.example.book.dto.book.BookDto;
import com.example.book.dto.book.BookDtoWithoutCategoryIds;
import com.example.book.dto.book.CreateBookRequestDto;
import com.example.book.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "id", ignore = true)
    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateBookFromDto(BookDto bookDto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);
}
