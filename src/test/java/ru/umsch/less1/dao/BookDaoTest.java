package ru.umsch.less1.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.umsch.less1.model.Author;
import ru.umsch.less1.model.Book;
import ru.umsch.less1.model.Genre;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@Import({AuthorDaoImpl.class, GenreDaoImpl.class, BookDaoImpl.class})
public class BookDaoTest {

    private static final String TEST_TITLE_1 = "testName";
    private static final String TEST_TITLE_2 = "testName2";
    private static final String TEST_AUTHOR_1 = "testAuthor";
    private static final String TEST_AUTHOR_2 = "testAuthor2";
    private static final String TEST_GENRE_1 = "testGenre";
    private static final String TEST_GENRE_2 = "testGenre2";


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookDaoImpl bookDao;


    @Test
    public void addNewBookTest()  {

        Book book = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);

        List<Book> books = bookDao.getAllBooks();

        assertThat(books)
                .hasSize(1)
                .contains(book);
    }

    @Test
    public void getAllBooksTest()  {
        Book book1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Book book2 = addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        List<Book> books = bookDao.getAllBooks();
        assertThat(books)
                .isNotEmpty()
                .hasSize(2)
                .contains(book1, book2);
    }

    @Test
    public void getAllTitlesTest()  {
        addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        List<String> titles = bookDao.getAllTitles();

        assertThat(titles)
                .hasSize(2)
                .contains(TEST_TITLE_1, TEST_TITLE_2);
    }

    @Test
    public void getBookByAuthorTest() throws Exception {
        Book expectedBook = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);
        Iterator<Author> iterator = expectedBook.getAuthors().iterator();
        Author author = iterator.next();

        List<Book> books = bookDao.getBooksByAuthor(author);
        Book resultBook = books.get(0);

        assertThat(resultBook).isEqualTo(expectedBook);
    }

    @Test
    public void getBookByIdTest()  {
        Book expectedBook = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        Long id = expectedBook.getId();
        Book resultBook = bookDao.getBookById(id);

        assertThat(resultBook).isEqualTo(expectedBook);
    }

    @Test
    public void getBookByTitleTest()  {
        Book expectedBook1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);

        List<Book> resultBooks = bookDao.getBooksByTitle(TEST_TITLE_1);

        assertThat(resultBooks)
                .isNotEmpty()
                .hasSize(1)
                .contains(expectedBook1);
    }

    @Test
    public void updateBookTitleByIdTest()  {
        Book book = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);

        bookDao.updateBookTitleById(book.getId(), TEST_TITLE_2);

        book = bookDao.getBookById(book.getId());
        String newTitle = book.getTitle();

        assertThat(newTitle).isEqualTo(TEST_TITLE_2);
    }

    @Test
    public void deleteBookByIdTest()  {
        Book book1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Book book2 = addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        List<Book> books = bookDao.getAllBooks();
        assertThat(books)
                .isNotEmpty()
                .hasSize(2)
                .contains(book1, book2);

        bookDao.deleteBookById(book1.getId());

        books = bookDao.getAllBooks();
        assertThat(books)
                .isNotEmpty()
                .hasSize(1)
                .contains(book2)
                .doesNotContain(book1);
    }

    @Test
    public void deleteAllTest()  {
        Book book1 = addTestBookToDb(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Book book2 = addTestBookToDb(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        List<Book> books = bookDao.getAllBooks();
        assertThat(books)
                .isNotEmpty()
                .hasSize(2)
                .contains(book1, book2);

        bookDao.deleteAll();

        books = bookDao.getAllBooks();
        assertThat(books).isEmpty();
    }

    private Book addTestBookToDb(String title, String authorName, String genreName) {
        Author author = new Author();
        author.setName(authorName);
        author = entityManager.persist(author);
        Set<Author> authors = new HashSet<>();
        authors.add(author);

        Genre genre = addTestGenre(genreName);

        Book book = new Book();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setGenre(genre);

        return entityManager.persist(book);
    }


    private Genre addTestGenre(String testName) {
        Genre genre = new Genre();
        genre.setGenreName(testName);
        return entityManager.persist(genre);
    }
}