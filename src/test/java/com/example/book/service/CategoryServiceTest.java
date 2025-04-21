package com.example.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.book.dto.category.CategoryDto;
import com.example.book.dto.category.CreateCategoryRequestDto;
import com.example.book.dto.category.UpdateCategoryRequestDto;
import com.example.book.mapper.CategoryMapper;
import com.example.book.model.Category;
import com.example.book.repository.CategoryRepository;
import com.example.book.service.category.CategoryServiceImpl;
import com.example.book.utils.TestDataUtil;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
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

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private List<Category> categoryList;
    private List<CategoryDto> categoryDtoList;

    @BeforeEach
    void setUp() {
        Category cookingCategory = new Category().setId(3L).setName("cooking")
                .setDescription("Cooking category");
        Category itGuidesCategory = new Category().setId(4L).setName("IT guides")
                .setDescription("IT guides category");
        Category novelCategory = new Category().setId(5L).setName("novel")
                .setDescription("Novel category");

        categoryList = List.of(cookingCategory, itGuidesCategory, novelCategory);

        CategoryDto cookingDto = new CategoryDto().setId(3L).setName("cooking")
                .setDescription("Cooking category");
        CategoryDto itGuidesDto = new CategoryDto().setId(4L).setName("IT guides")
                .setDescription("IT guides category");
        CategoryDto novelDto = new CategoryDto().setId(5L).setName("novel")
                .setDescription("Novel category");

        categoryDtoList = List.of(cookingDto, itGuidesDto, novelDto);
    }

    @Test
    @DisplayName("Ensure that retrieving all categories returns the expected CategoryDto page")
    public void retrieveAllCategories_returnsExpectedDtoPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(categoryList);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        categoryList.forEach(cat ->
                when(categoryMapper.toDto(cat))
                        .thenReturn(categoryDtoList.stream()
                                .filter(dto -> dto.getId().equals(cat.getId()))
                                .findFirst().orElseThrow())
        );

        Page<CategoryDto> actualPage = categoryService.findAll(pageable);

        assertThat(actualPage.getContent())
                .containsExactlyElementsOf(categoryDtoList);
        verify(categoryRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Ensure that retrieving a category by valid id returns the correct CategoryDto")
    public void retrieveCategoryById_returnsExpectedDto() {
        Long validId = 3L;
        Category category = categoryList.stream()
                .filter(cat -> cat.getId().equals(validId))
                .findFirst().orElseThrow();
        CategoryDto expectedDto = categoryDtoList.stream()
                .filter(dto -> dto.getId().equals(validId))
                .findFirst().orElseThrow();

        when(categoryRepository.findById(validId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        CategoryDto actualDto = categoryService.getById(validId);

        assertThat(actualDto).isEqualTo(expectedDto);
        verify(categoryRepository).findById(validId);
    }

    @Test
    @DisplayName("Expect an exception when retrieving a category with an invalid id")
    public void retrieveCategoryById_withInvalidId_throwsException() {
        Long invalidId = 10L;

        assertThatThrownBy(() -> categoryService.getById(invalidId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Category with id " + invalidId + " not found");
        verify(categoryRepository).findById(invalidId);
    }

    @Test
    @DisplayName("Ensure that saving a category with a valid create"
            + " request returns the expected CategoryDto")
    public void createCategory_withValidRequest_returnsExpectedDto() {
        CreateCategoryRequestDto createRequest = TestDataUtil.createCategoryRequestDto();
        Category categoryToSave = new Category()
                .setName(createRequest.getName())
                .setDescription(createRequest.getDescription());
        CategoryDto expectedDto = TestDataUtil.mapToCategoryDto(createRequest);

        when(categoryMapper.toModel(createRequest)).thenReturn(categoryToSave);
        when(categoryRepository.save(categoryToSave)).thenReturn(categoryToSave);
        when(categoryMapper.toDto(categoryToSave)).thenReturn(expectedDto);

        CategoryDto actualDto = categoryService.save(createRequest);

        assertThat(actualDto).isEqualTo(expectedDto);

        verify(categoryMapper).toModel(createRequest);
        verify(categoryRepository).save(categoryToSave);
        verify(categoryMapper).toDto(categoryToSave);
    }

    @Test
    @DisplayName("Ensure that updating a category with a valid id returns the updated CategoryDto")
    public void updateCategory_withValidId_returnsUpdatedDto() {
        Long validId = 4L;
        UpdateCategoryRequestDto updateRequest = TestDataUtil.updateCategoryRequestDto();

        Category existingCategory = categoryList.stream()
                .filter(cat -> cat.getId().equals(validId))
                .findFirst().orElseThrow();
        CategoryDto expectedDto = TestDataUtil.mapToCategoryDto(validId, updateRequest);

        when(categoryRepository.findById(validId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(existingCategory);
        when(categoryMapper.toDto(existingCategory)).thenReturn(expectedDto);

        CategoryDto actualDto = categoryService.update(validId, updateRequest);

        assertThat(actualDto).isEqualTo(expectedDto);
        verify(categoryRepository).findById(validId);
        verify(categoryRepository).save(existingCategory);
        verify(categoryMapper).toDto(existingCategory);
    }

    @Test
    @DisplayName("Expect an exception when attempting to update a category with an invalid id")
    public void updateCategory_withInvalidId_throwsException() {
        Long invalidId = 20L;
        UpdateCategoryRequestDto updateRequest = TestDataUtil.updateCategoryRequestDto();

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> categoryService.update(invalidId, updateRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Category with id " + invalidId + " not found");
        verify(categoryRepository).findById(invalidId);
    }

    @Test
    @DisplayName("Ensure that calling deleteById with a valid id invokes"
            + " the repository's delete method")
    public void deleteCategory_withValidId_callsRepositoryDelete() {
        Long validId = 3L;

        when(categoryRepository.existsById(validId)).thenReturn(true);
        categoryService.deleteById(validId);

        verify(categoryRepository).existsById(validId);
        verify(categoryRepository, times(1)).deleteById(validId);
    }
}

