package com.example.book.mapper;

import com.example.book.dto.BookDto;
import com.example.book.dto.CreateBookRequestDto;
import com.example.book.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);
    Book toModel(CreateBookRequestDto requestDto);
}
