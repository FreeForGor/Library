package ru.umsch.less1.dao;

import ru.umsch.less1.model.Author;

import java.util.List;

public interface AuthorDao {
    List<String> getAllAuthorsNames();
    Author getAuthorByName(String name);
    Author getAuthorById(Long id);

    Author addNewAuthor(Author author);

    int deleteAuthor(Author author);
    int deleteAuthorById(Long id);
    int deleteAll();
}
