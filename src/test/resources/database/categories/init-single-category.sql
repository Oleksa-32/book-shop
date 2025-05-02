TRUNCATE TABLE books_categories;
TRUNCATE TABLE categories RESTART IDENTITY;

INSERT INTO categories (name, description)
VALUES ('language', NULL);