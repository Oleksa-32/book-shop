package com.example.book;

import com.example.book.model.Book;
import com.example.book.service.BookService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookShopApplication.class, args);
    }

    @Bean
    public CommandLineRunner demoRunner(BookService bookService) {
        return args -> {
            Book book = new Book();
            book.setTitle("Spring in Action");
            book.setAuthor("Craig Walls");
            book.setIsbn("1234567890");
            book.setPrice(new BigDecimal("39.99"));
            book.setDescription("Guide to Spring Boot");
            book.setCoverImage("cover_image_url");
            bookService.save(book);

            List<Book> allBooks = bookService.findAll();
            allBooks.forEach(b -> System.out.println("Book Title: " + b.getTitle()));
        };
    }
}
