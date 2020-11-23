
INSERT INTO genres (genre_name) VALUES ('детектив');
INSERT INTO genres (genre_name) VALUES ('роман');
INSERT INTO genres (genre_name) VALUES ('драма');
INSERT INTO genres (genre_name) VALUES ('сатира');
INSERT INTO genres (genre_name) VALUES ('комедия');

INSERT INTO authors (author_name) VALUES ('А.Конан Дойл');
INSERT INTO authors (author_name) VALUES ('Л.Толстой');
INSERT INTO authors (author_name) VALUES ('Ф.Достоевский');
INSERT INTO authors (author_name) VALUES ('М.Булгаков');
INSERT INTO authors (author_name) VALUES ('А.Чехов');


INSERT INTO users (user_name) VALUES ('Васян');
INSERT INTO users (user_name) VALUES ('Стасян');
INSERT INTO users (user_name) VALUES ('Сосян');

INSERT INTO books (title, genre_id)
VALUES ('Шерлок Холмс', (SELECT id FROM genres WHERE genre_name = 'детектив'));

INSERT INTO books (title, genre_id)
VALUES ('Война и мир', (SELECT id FROM genres WHERE genre_name = 'роман'));

INSERT INTO books (title, genre_id)
VALUES ('Преступление и наказание', (SELECT id FROM genres WHERE genre_name = 'драма'));

INSERT INTO books (title, genre_id)
VALUES ('Мастер и Маргарита', (SELECT id FROM genres WHERE genre_name = 'сатира'));

INSERT INTO books (title, genre_id)
VALUES ('Идиот', (SELECT id FROM genres WHERE genre_name = 'драма'));




INSERT INTO books_authors (authors_id, books_id)
VALUES (
           (SELECT id FROM authors WHERE author_name = 'А.Конан Дойл'),
           (SELECT id FROM books WHERE title = 'Шерлок Холмс'));

INSERT INTO books_authors (authors_id, books_id)
VALUES (
           (SELECT id FROM authors WHERE author_name = 'Л.Толстой'),
           (SELECT id FROM books WHERE title = 'Война и мир')
       );

INSERT INTO books_authors (authors_id, books_id)
VALUES (
           (SELECT id FROM authors WHERE author_name = 'Ф.Достоевский'),
           (SELECT id FROM books WHERE title = 'Преступление и наказание')
       );

INSERT INTO books_authors (authors_id, books_id)
VALUES (
           (SELECT id FROM authors WHERE author_name = 'М.Булгаков'),
           (SELECT id FROM books WHERE title = 'Мастер и Маргарита')
       );

INSERT INTO books_authors (authors_id, books_id)
VALUES (
           (SELECT id FROM authors WHERE author_name = 'А.Чехов'),
           (SELECT id FROM books WHERE title = 'Идиот')
       );

INSERT INTO book_comments (comment_text, comment_date, user_id, book_id)
VALUES ('Нормас чтиво, под пивко сойдет',
        '2020-11-23 12:12:12',
        (SELECT id FROM users WHERE user_name = 'Васян'),
        (SELECT id FROM books WHERE title = 'Мастер и Маргарита'));

INSERT INTO book_comments (comment_text, comment_date, user_id, book_id)
VALUES ('Классека',
        '2020-11-23 10:10:10',
        (SELECT id FROM users WHERE user_name = 'Стасян'),
        (SELECT id FROM books WHERE title = 'Преступление и наказание'));

INSERT INTO book_comments (comment_text, comment_date, user_id, book_id)
VALUES ('Орали всем селом',
        '2020-11-23 12:24:36',
        (SELECT id FROM users WHERE user_name = 'Сосян'),
        (SELECT id FROM books WHERE title = 'Идиот'));