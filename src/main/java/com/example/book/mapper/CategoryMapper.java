package com.example.book.mapper;

import com.example.book.config.MapperConfig;
import com.example.book.dto.CategoryDto;
import com.example.book.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    CategoryDto toDto(Category category);
    Category toModel(CategoryDto categoryDto);
    void updateFromDto()
}
