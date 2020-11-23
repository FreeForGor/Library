package ru.umsch.less1.dao;

import ru.umsch.less1.model.Author;
import ru.umsch.less1.model.Book;

import java.util.List;

public interface BookDao {

    List<String> getAllTitles();

    List<Book> getAllBooks();
    List<Book> getBooksByAuthor(Author author);
    List<Book> getBooksByTitle(String title);
    Book getBookById(Long id);

    Book addNewBook(Book book);

    boolean updateBookTitleById(Long id, String newTitle);

    boolean deleteBookById(Long id);
    int deleteAll();
}
