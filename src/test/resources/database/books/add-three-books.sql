TRUNCATE TABLE books RESTART IDENTITY;


INSERT INTO books (id, title, author, isbn, price)
VALUES
    (1, 'Book 1', 'Author A', 9783161484100, 20.99),
    (2, 'Book 2', 'Author B', 9783161484117, 25.99),
    (3, 'Book 3', 'Author C', 9783161484124, 17.99);
ALTER TABLE books ALTER COLUMN id RESTART WITH 4;