INSERT INTO books (id, title, author, isbn, price)
VALUES (4, 'Sample Book 4', 'Author A', '1236567163331', 19.99),
       (5, 'Sample Book 5', 'Author B', '1236567163330', 34.99),
       (6, 'Sample Book 6', 'Author C', '1236567163332', 12.99);

INSERT INTO categories (id, name, description)
VALUES (3, 'cooking', null),
       (4, 'IT guides', null),
       (5, 'novel', null),
       (6, 'language', null);

INSERT INTO books_categories (book_id, category_id)
VALUES (4, 3),
       (5, 4),
       (6, 4),
       (6, 5);