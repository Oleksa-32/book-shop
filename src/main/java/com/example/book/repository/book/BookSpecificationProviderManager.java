package com.example.book.repository.book;

import com.example.book.model.Book;
import com.example.book.repository.SpecificationProvider;
import com.example.book.repository.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(b -> b.getKey().equals(key))
                .findFirst()
                .orElseThrow(
                        () -> new RuntimeException("Can't find correct"
                                + " specification provider for key" + key));
    }
}
