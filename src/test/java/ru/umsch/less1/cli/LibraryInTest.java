package ru.umsch.less1.cli;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.context.junit4.SpringRunner;
import ru.umsch.less1.dao.*;
import ru.umsch.less1.model.*;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class LibraryInTest {

    private static final String TEST_TITLE_1 = "testName";
    private static final String TEST_TITLE_2 = "testName2";
    private static final String TEST_AUTHOR_1 = "testAuthor";
    private static final String TEST_AUTHOR_2 = "testAuthor2";
    private static final String TEST_GENRE_1 = "testGenre";
    private static final String TEST_GENRE_2 = "testGenre2";
    private static final String TEST_USER = "testUser";
    private static final String TEST_TEXT_1 = "testText1";
    private static final String TEST_TEXT_2 = "testText2";

    @Autowired
    private Shell shell;

    @Autowired
    private BookDaoImpl bookDao;

    @Autowired
    private AuthorDaoImpl authorDao;

    @Autowired
    private GenreDaoImpl genreDao;

    @Autowired
    private CommentDaoImpl commentDao;

    @Autowired
    private UserDaoImpl userDao;

    @Before
    public void init() {
        bookDao.deleteAll();
        genreDao.deleteAll();
        authorDao.deleteAll();
        userDao.deleteAll();
        commentDao.deleteAll();
    }

    @Test
    public void getAllAuthorsNamesTest() throws Exception {
        addTestAuthor(TEST_AUTHOR_1);
        addTestAuthor(TEST_AUTHOR_2);

        Object res = shell.evaluate(() -> "all-names");

        String result = res.toString();
        System.out.println(result);

        String expected = "[" + TEST_AUTHOR_1 + ", " + TEST_AUTHOR_2 + "]";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void getAllBooksTest() throws Exception {
        Book book1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Book book2 = addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        Object res = shell.evaluate(() -> "all-books");

        String result = res.toString();
        String expected = "Book idAuthor(s)  Title    Genre     \n";

        assertThat(result).contains(expected,
                String.valueOf(book1.getId()), TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1,
                String.valueOf(book2.getId()), TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);
    }

    @Test
    public void getAllGenresTest() throws Exception {
        addTestGenre(TEST_GENRE_1);
        addTestGenre(TEST_GENRE_2);

        Object res = shell.evaluate(() -> "all-genres");

        String result = res.toString();
        String expected = "[" + TEST_GENRE_1 + ", " + TEST_GENRE_2 + "]";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void getAllCommentsForBookTest() throws Exception {
        Book book = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        User user = userDao.addUser(new User(TEST_USER));
        Comment comment1 = new Comment(book, user, TEST_TEXT_1);
        Comment comment2 = new Comment(book, user, TEST_TEXT_2);

        commentDao.addComment(comment1);
        commentDao.addComment(comment2);

        Object result = shell.evaluate(() -> "comm-by " + book.getId());

        assertThat(result.toString())
                .isNotNull()
                .contains(TEST_TEXT_1, TEST_TEXT_2);
    }

    @Test
    public void addNewBookOneAuthorWhenSuccessfulTest() throws Exception {
        shell.evaluate(() -> "add-book " + TEST_GENRE_1 + " " + TEST_TITLE_1 + " " + TEST_AUTHOR_1);

        Author author = authorDao.getAuthorByName(TEST_AUTHOR_1);
        assertThat(author).isNotNull();

        Book book = bookDao.getBooksByAuthor(author).get(0);

        assertThat(book.getGenre().getGenreName()).isEqualTo(TEST_GENRE_1);
        assertThat(book.getTitle()).isEqualTo(TEST_TITLE_1);
    }

    @Test
    public void addNewBookOneAuthorWhenAlreadyExistsTest() throws Exception {
        addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);

        Object res = shell.evaluate(() -> "add-book " + TEST_GENRE_1 + " " + TEST_TITLE_1 + " " + TEST_AUTHOR_1);

        String result = res.toString();
        String expected = "Книга существует";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void addNewGenreWhenSuccessfulTest() throws Exception {
        shell.evaluate(() -> "add-genre " + TEST_GENRE_1);

        Genre genre = genreDao.getGenreByName(TEST_GENRE_1);

        assertThat(genre).isNotNull();
    }

    @Test
    public void addNewGenreWhenAlreadyExistsTest() throws Exception {
        addTestGenre(TEST_GENRE_1);

        Object res = shell.evaluate(() -> "add-genre " + TEST_GENRE_1);

        String result = res.toString();
        String expected = "Жанр существует";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void addNewAuthorTest() throws Exception {
        shell.evaluate(() -> "add-author " + TEST_AUTHOR_1);

        Author author = authorDao.getAuthorByName(TEST_AUTHOR_1);

        assertThat(author).isNotNull();
    }

    @Test
    public void addNewCommentToBookTest() throws Exception {
        Book book = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Long id = book.getId();

        shell.evaluate(() -> "add-comm " + id + " " + TEST_USER + " " + TEST_TEXT_1);

        List<String> result = commentDao.getCommentsByBookId(id);
        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .contains(TEST_TEXT_1);
    }

    @Test
    public void updateBookTitleByIdTest() throws Exception {
        Book book = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Long id = book.getId();

        shell.evaluate(() -> "upd-title-id " + id + " " + TEST_TITLE_2);

        book = bookDao.getBookById(book.getId());
        assertThat(book.getTitle()).isEqualTo(TEST_TITLE_2);
    }

    @Test
    public void updateBookTitleByIdWhenNoBookTest() throws Exception {
        Object res = shell.evaluate(() -> "upd-title-id " + 100 + " " + TEST_TITLE_2);

        String result = res.toString();
        String expected = "Книги не существует";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void updateCommentByIdTest() throws Exception {
        Book book = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        User user = userDao.addUser(new User(TEST_USER));
        Comment comment = new Comment(book, user, TEST_TEXT_1);
        commentDao.addComment(comment);

        shell.evaluate(() -> "upd-comm " + comment.getId() + " " + TEST_TEXT_2);

        Comment updatedComment = commentDao.getCommentById(comment.getId());
        assertThat(updatedComment.getCommentText()).isEqualTo(TEST_TEXT_2);
    }

    @Test
    public void deleteBookByIdTest() throws Exception {
        Book book1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Book book2 = addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        Long id = book2.getId();
        shell.evaluate(() -> "del-book " + id);

        List<Book> books = bookDao.getAllBooks();
        assertThat(books)
                .hasSize(1)
                .contains(book1)
                .doesNotContain(book2);
    }

    @Test
    public void deleteBookByIdWhenNoBookTest() throws Exception {
        Object res = shell.evaluate(() -> "del-book " + 100);

        String result = res.toString();
        String expected = "Книги не существует";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void deleteAuthorByIdTest() throws Exception {
        Author author = addTestAuthor(TEST_AUTHOR_1);
        addTestAuthor(TEST_AUTHOR_2);

        shell.evaluate(() -> "del-auth " + author.getId());

        List<String> authors = authorDao.getAllAuthorsNames();
        assertThat(authors)
                .hasSize(1)
                .contains(TEST_AUTHOR_2)
                .doesNotContain(TEST_AUTHOR_1);
    }

    @Test
    public void deleteAuthorByIdWhenNoAuthorTest() throws Exception {
        Object res = shell.evaluate(() -> "del-auth " + 100);

        String result = res.toString();
        String expected = "Автора не существует";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void deleteGenreTest() throws Exception {
        Genre genre1 = addTestGenre(TEST_GENRE_1);
        Genre genre2 = addTestGenre(TEST_GENRE_2);

        shell.evaluate(() -> "del-genre " + TEST_GENRE_1);

        List<Genre> genres = genreDao.getAllGenres();
        assertThat(genres)
                .hasSize(1)
                .contains(genre2)
                .doesNotContain(genre1);
    }

    @Test
    public void deleteCommentByIdTest() throws Exception {
        Book book = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        User user = userDao.addUser(new User(TEST_USER));
        Comment comment = new Comment(book, user, TEST_TEXT_1);
        commentDao.addComment(comment);
        Long id = comment.getId();

        shell.evaluate(() -> "del-comm " + id);

        Comment testComment = commentDao.getCommentById(id);
        assertThat(testComment).isNull();
    }


    private Book addTestBookToDb(String title, String authorName, String genreName) {
        Author author = new Author();
        author.setName(authorName);
        author = authorDao.addNewAuthor(author);
        Set<Author> authors = new HashSet<>();
        authors.add(author);

        Genre genre = addTestGenre(genreName);

        Book book = new Book();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setGenre(genre);

        return bookDao.addNewBook(book);
    }


    private Author addTestAuthor(String testName) {
        Author author = new Author();
        author.setName(testName);
        return authorDao.addNewAuthor(author);
    }


    private Genre addTestGenre(String testName) {
        Genre genre = new Genre();
        genre.setGenreName(testName);
        genre = genreDao.addGenre(genre);
        return genre;
    }
}