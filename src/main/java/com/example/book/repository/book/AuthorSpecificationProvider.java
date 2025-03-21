package com.example.book.repository.book;

import com.example.book.model.Book;
import com.example.book.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider {
    @Override
    public String getKey() {
        return "author";
    }

    @Override
    public Specification<Book> getSpecification(String[] param) {
        return (root, query, cb) ->
                root.get("author").in(Arrays.stream(param).toArray());
    }
}
