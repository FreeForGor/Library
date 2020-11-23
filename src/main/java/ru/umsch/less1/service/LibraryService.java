package ru.umsch.less1.service;

import ru.umsch.less1.model.Author;
import ru.umsch.less1.model.Book;
import ru.umsch.less1.model.Comment;
import ru.umsch.less1.model.Genre;

import java.util.List;

public interface LibraryService {

    List<Book> getAllBooks();
    List<String> getAllAuthorsNames();
    List<String> getAllGenres();
    List<Book> getBooksByAuthorsName(String name);
    List<String> getAllComments(Long bookId);

    boolean addNewGenre(Genre genre);
    boolean addNewBook(Book book);
    boolean addNewAuthor(Author author);
    boolean addComment(Long bookId, String userName, String comment);

    boolean updateBookTitleById(Long id, String newTitle);
    boolean updateComment(Comment comment);

    boolean deleteBookById(Long id);
    boolean deleteAuthorById(Long id);
    boolean deleteGenre(String genreName);
    boolean deleteCommentById(Long id);
    void deleteAll();
}
