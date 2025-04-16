INSERT INTO categories (id, name, description)
VALUES (1, 'sci-fi', null),
       (2, 'fantasy', null);

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1),
       (2, 2),
       (3, 1),
       (3, 2);