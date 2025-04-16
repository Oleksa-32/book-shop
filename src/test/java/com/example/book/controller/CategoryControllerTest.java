package com.example.book.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.book.dto.book.BookDtoWithoutCategoryIds;
import com.example.book.dto.category.CategoryDto;
import com.example.book.dto.category.CreateCategoryRequestDto;
import com.example.book.dto.category.UpdateCategoryRequestDto;
import com.example.book.utils.TestDataUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class CategoryControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext webApplicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/delete-all-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories/init-for-category-tests.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        tearDown(dataSource);
    }

    @SneakyThrows
    static void tearDown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/delete-all.sql")
            );
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create a new category")
    void createCategory_ValidRequestDto_Success() throws Exception {
        CreateCategoryRequestDto requestDto = TestDataUtil.createCategoryRequestDto();

        CategoryDto categoryDto = TestDataUtil.mapToCategoryDto(requestDto);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(categoryDto, actual, "id"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get all categories")
    void getAll_GivenCategories_ShouldReturnAllBooks() throws Exception {
        List<CategoryDto> expected = TestDataUtil.categoryDtoList();

        MvcResult result = mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsByteArray());

        JsonNode contentNode = root.has("content") ? root.get("content") : root;

        CategoryDto[] actual = objectMapper.treeToValue(contentNode, CategoryDto[].class);

        assertNotNull(actual);
        assertEquals(expected.size(), actual.length);
        assertEquals(expected, Arrays.asList(actual));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get all books by category id")
    void getBooksByCategoryId_GivenCategoryId_ShouldReturnBooksWithoutCategoryIds()
            throws Exception {
        long categoryId = 4L;
        List<BookDtoWithoutCategoryIds> expected = TestDataUtil.bookDtoWithoutCategoryIdsList();

        MvcResult result = mockMvc.perform(get("/categories/" + categoryId + "/books"))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsByteArray());
        JsonNode contentNode = root.has("content") ? root.get("content") : root;

        BookDtoWithoutCategoryIds[] actual = objectMapper.treeToValue(contentNode,
                BookDtoWithoutCategoryIds[].class);

        assertNotNull(actual);
        assertEquals(expected.size(), actual.length);
        assertEquals(expected, Arrays.asList(actual));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    @DisplayName("Update category by id")
    void updateBookById_WithValidInput_ReturnsUpdatedBook() throws Exception {
        Long categoryId = 4L;
        UpdateCategoryRequestDto updateRequestDto = TestDataUtil.updateCategoryRequestDto();

        CategoryDto expected = TestDataUtil.mapToCategoryDto(categoryId, updateRequestDto);

        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        MvcResult result = mockMvc.perform(put("/categories/" + expected.getId())
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get category by id")
    void getById_ValidId_ShouldReturnCategoryDto() throws Exception {
        CategoryDto expected = TestDataUtil.categoryDto();

        MvcResult result = mockMvc.perform(get("/categories/" + expected.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), CategoryDto.class);
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    @DisplayName("Delete category by id")
    void deleteCategoryById_WithValidId_NoContent() throws Exception {
        //create a new category
        CreateCategoryRequestDto createDto = TestDataUtil.createCategoryRequestDto();
        String createJson = objectMapper.writeValueAsString(createDto);

        MvcResult createResult = mockMvc.perform(post("/categories")
                        .content(createJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto created = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                CategoryDto.class
        );
        Long newCategoryId = created.getId();
        assertNotNull(newCategoryId);

        mockMvc.perform(get("/categories/" + newCategoryId))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/categories/" + newCategoryId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/categories/" + newCategoryId))
                .andExpect(status().isNotFound());
    }
}
