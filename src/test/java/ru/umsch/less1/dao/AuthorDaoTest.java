package ru.umsch.less1.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.umsch.less1.model.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext // база пересоздается каждый тест
@Import({AuthorDaoImpl.class})
public class AuthorDaoTest {

    private static final String TEST_NAME_1 = "testName";
    private static final String TEST_NAME_2 = "testName2";

    @Autowired
    private AuthorDaoImpl dao;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void addNewAuthorTest() {
        addAuthorTest(TEST_NAME_1);
        List<String> authors = dao.getAllAuthorsNames();
        assertThat(authors)
                .isNotEmpty()
                .hasSize(1)
                .contains(TEST_NAME_1);
    }

    @Test
    public void getAllAuthorsNamesTest() {
        List<String> authors = dao.getAllAuthorsNames();
        assertThat(authors).isEmpty();

        addAuthorTest(TEST_NAME_1);
        addAuthorTest(TEST_NAME_2);

        authors = dao.getAllAuthorsNames();

        assertThat(authors)
                .hasSize(2)
                .contains(TEST_NAME_1, TEST_NAME_2);
    }

    @Test
    public void getAuthorByNameTest() {
        addAuthorTest(TEST_NAME_1);
        Author author = dao.getAuthorByName(TEST_NAME_1);
        assertThat(author.getName()).isEqualTo(TEST_NAME_1);
    }

    @Test
    public void deleteAuthorTest() {
        addAuthorTest(TEST_NAME_1);
        addAuthorTest(TEST_NAME_2);

        List<String> authors = dao.getAllAuthorsNames();
        assertThat(authors)
                .hasSize(2)
                .contains(TEST_NAME_1, TEST_NAME_2);

        Author author = dao.getAuthorByName(TEST_NAME_1);
        dao.deleteAuthor(author);

        authors = dao.getAllAuthorsNames();
        assertThat(authors)
                .hasSize(1)
                .contains(TEST_NAME_2)
                .doesNotContain(TEST_NAME_1);
    }

    @Test
    public void deleteAllTest() {
        addAuthorTest(TEST_NAME_1);
        addAuthorTest(TEST_NAME_2);

        List<String> authors = dao.getAllAuthorsNames();
        assertThat(authors)
                .hasSize(2)
                .contains(TEST_NAME_1, TEST_NAME_2);

        dao.deleteAll();

        authors = dao.getAllAuthorsNames();
        assertThat(authors).isEmpty();
    }

    private void addAuthorTest(String testName) {
        Author author = new Author();
        author.setName(testName);
        entityManager.persist(author);
    }

}