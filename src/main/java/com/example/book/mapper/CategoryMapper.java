package com.example.book.mapper;

import com.example.book.config.MapperConfig;
import com.example.book.dto.category.CategoryDto;
import com.example.book.dto.category.CreateCategoryRequestDto;
import com.example.book.dto.category.UpdateCategoryRequestDto;
import com.example.book.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    Category toModel(CreateCategoryRequestDto createCategoryRequestDto);

    @Mapping(target = "id", ignore = true)
    void updateFromDto(UpdateCategoryRequestDto updateCategoryRequestDto,
                       @MappingTarget Category category);

    @Named("categoriesByIds")
    default Set<Category> categoriesByIds(Set<Long> ids) {
        if (ids == null) {
            return null;
        }
        return ids.stream().map(id -> {
            Category category = new Category();
            category.setId(id);
            return category;
        }).collect(Collectors.toSet());
    }
}
