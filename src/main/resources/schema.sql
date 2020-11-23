DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS books CASCADE;
DROP TABLE IF EXISTS authors CASCADE;
DROP TABLE IF EXISTS books_authors CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS book_comments CASCADE;

CREATE TABLE genres(
                       id SERIAL PRIMARY KEY,
                       genre_name VARCHAR(255) UNIQUE
);

CREATE TABLE books(
                      id SERIAL PRIMARY KEY ,
                      title VARCHAR(255),
                      genre_id INTEGER REFERENCES genres(id)
);

CREATE TABLE authors(
                        id SERIAL PRIMARY KEY,
                        author_name VARCHAR(255)
);

CREATE TABLE books_authors(
                              authors_id INTEGER REFERENCES authors(id) ON DELETE CASCADE,
                              books_id INTEGER REFERENCES books(id) ON DELETE CASCADE,
                              PRIMARY KEY (authors_id, books_id)
);

CREATE TABLE users (
                       id SERIAL PRIMARY KEY ,
                       user_name VARCHAR(255) UNIQUE
);

CREATE  TABLE book_comments (
                                id SERIAL PRIMARY KEY ,
                                comment_text TEXT ,
                                comment_date TIMESTAMP ,
                                user_id INTEGER REFERENCES users(id) ON DELETE CASCADE ,
                                book_id INTEGER REFERENCES books(id) ON DELETE CASCADE
);