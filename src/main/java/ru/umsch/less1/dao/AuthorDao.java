package ru.umsch.less1.dao;

import ru.umsch.less1.model.Author;

import java.util.List;

public interface AuthorDao {
    List<String> getAllAuthorsNames();
    Author getAuthorByName(String name);
    Author getAuthorById(Long id);

    Author addNewAuthor(Author author);

    void deleteAuthor(Author author);
    boolean deleteAuthorById(Long id);
    int deleteAll();
}
