package com.example.book.controller;

import com.example.book.dto.book.BookDto;
import com.example.book.dto.book.CreateBookRequestDto;
import com.example.book.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookControllerTest {
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
        try (Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/delete-all.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/add-tree-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/add-two-categories.sql")
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
        try(Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/delete-all-books.sql")
            );
        }
    }

    @Test
    @Sql(
            scripts = "classpath:database/books/delete-title-books.sql"
    )
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create a new book")
    void createBook_ValidRequestDto_Success() throws Exception{
        CreateBookRequestDto requestDto = TestDataUtil.createBookRequesDto();

        BookDto bookDto = TestDataUtil.mapToBookDto(requestDto);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books"))
                .connect(jsonRequest)
                .connectType(MediaType.APPLICATION_JSON)
                .andExpect(status.isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(bookDto, actual, "id"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get all books")
    void getAll_GivenBooks_ShouldReturnAllBooks() throws Exception {
        List<BookDto> expected = TestDataUtil.bookDtoList();

        MvcResult result = mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDto[].class);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get book by id")
    void getById_ValidId_ShouldReturnBookDto() throws  Exception {
        BookDto expected = TestDataUtil.bookDto();

        MvcResult result = mockMvc.perform(get("/books/" + expected.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper,readValue(result.getResponse()
                .getContentAsByteArray(), BookDto.class);
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete book by id")
    void deleteBookById_WithValidId_NoContent() throws Exception {
        Long bookId = 1L;

        MvcResult result = mockMvc.perform(delete("/books/" + bookId))
                .andExpect(status().isNoContent())
                .andReturn();
         assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }

    void updateBookById_WithValidInput_ReturnsUpdateBook() throws Exception {
        Long bookId = 3L;
        //Update
    }


    void search_WithValidInput_ReturnsSelectedList() throws Exception {
        List<BookDto> list = TestDataUtil.bookDtoList();
        String authorFilter = "Author A";

        List<BookDto> expected = list.stream()
                .filter(b -> b.getAuthor().equals(authorFilter))
                .toList();

        MvcResult result = mockMvc.perform(get("/books/search")
                .param("author", authorFilter)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto[].class);

        assertNotNull(actual);
        assertEquals(expected, Arrays.asList(actual));

    }
}

