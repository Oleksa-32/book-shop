package com.example.book.service;

import com.example.book.dto.category.CategoryDto;
import com.example.book.dto.category.CreateCategoryRequestDto;
import com.example.book.dto.category.UpdateCategoryRequestDto;
import com.example.book.mapper.CategoryMapper;
import com.example.book.model.Category;
import com.example.book.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category with id " + id + " not found"));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto createCategoryRequestDto) {
        Category category = categoryMapper.toModel(createCategoryRequestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, UpdateCategoryRequestDto updateCategoryRequestDto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category with id " + id + " not found"));
        return categoryMapper.toDto(categoryRepository.save(existingCategory));
    }

    @Override
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category with id " + id + " not found");
        }
        categoryRepository.deleteById(id);
    }
}
