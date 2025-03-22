package com.example.book.repository.book;

import com.example.book.model.Book;
import com.example.book.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    private static final String ISBN_KEY = "isbn";

    @Override
    public String getKey() {
        return ISBN_KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] param) {
        return (root, query, cb) ->
                root.get("isbn").in(Arrays.stream(param).toArray());
    }
}
