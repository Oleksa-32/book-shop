package com.example.book.repository.book;

import com.example.book.dto.book.BookSearchParametersDto;
import com.example.book.model.Book;
import com.example.book.repository.SpecificationBuilder;
import com.example.book.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String AUTHOR_KEY = "author";
    private static final String TITLE_KEY = "title";
    private static final String ISBN_KEY = "isbn";

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Specification<Book> specification = Specification.where(null);
        if (searchParametersDto.author() != null && searchParametersDto.author().length > 0) {
            specification.and(specificationProviderManager.getSpecificationProvider(AUTHOR_KEY)
                    .getSpecification(searchParametersDto.author()));
        }
        if (searchParametersDto.title() != null) {
            specification.and(specificationProviderManager.getSpecificationProvider(TITLE_KEY)
                    .getSpecification(searchParametersDto.title()));
        }
        if (searchParametersDto.isbn() != null) {
            specification.and(specificationProviderManager.getSpecificationProvider(ISBN_KEY)
                    .getSpecification(searchParametersDto.isbn()));
        }
        return specification;
    }
}
