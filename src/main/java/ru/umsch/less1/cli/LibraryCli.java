package ru.umsch.less1.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.*;
import ru.umsch.less1.model.Author;
import ru.umsch.less1.model.Book;
import ru.umsch.less1.model.Genre;
import ru.umsch.less1.service.LibraryServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.shell.table.CellMatchers.table;

@ShellComponent
public class LibraryCli {

    private final LibraryServiceImpl libraryService;

    @Autowired
    public LibraryCli(LibraryServiceImpl libraryService) {
        this.libraryService = libraryService;
    }

    @ShellMethod(value = "Показать всех авторов", key = "all-names")
    public List<String> getAllAuthorsNames() {
        return libraryService.getAllAuthorsNames();
    }

    @ShellMethod(value = "Показать все книги", key = "all-books")
    public String getAllBooks() {
        return getTableFromList(libraryService.getAllBooks());
    }

    @ShellMethod(value = "Показать все жанры", key = "all-genres")
    public List<String> getAllGenres() {
        return libraryService.getAllGenres();
    }

    @ShellMethod(value = "Поиск книг по имени атора", key = "book-of")
    public String getBooksByAuthorsName(
            @ShellOption(help = "имя автора") String name) {
        return getTableFromList(libraryService.getBooksByAuthorsName(name));
    }

    @ShellMethod(value = "Добавлание новых книг", key = "add-book")
    public String addNewBook(
            @ShellOption(help = "жанр") String genreName,
            @ShellOption(help = "название") String title,
            @ShellOption(help = "автор") String authors) {

        String[] authorsArr = authors.split(",");
        List<Author> authorList = Arrays.stream(authorsArr).map(Author::new).collect(Collectors.toList());
        Genre genre = new Genre(genreName);
        boolean isSuccessful = libraryService.addNewBook(new Book(title, genre, authorList));
        if (isSuccessful) {
            return "Новая книга добавлена";
        } else {
            return "Накая книга уже есть!";
        }
    }

    @ShellMethod(value = "Добавление нового жанра", key = "add-genre")
    public String addNewGenre(
            @ShellOption(help = "Название жанра") String genre) {
        boolean isSuccessful = libraryService.addNewGenre(new Genre(genre));
        if (isSuccessful) {
            return "Новый жанр добавлен";
        } else {
            return "Жанр уже существует";
        }
    }

    @ShellMethod(value = "Добавение нового автора", key = "add-author")
    public void addNewAuthor(
            @ShellOption(help = "имя автора") String authorName) {
        libraryService.addNewAuthor(new Author(authorName));
    }

    @ShellMethod(value = "Обновление названия книги по id", key = "upd-title-id")
    public String updateBookTitleById(
            @ShellOption(help = "id") Long id,
            @ShellOption(help = "новое название") String newTitle) {
        boolean isSuccessful = libraryService.updateBookTitleById(id, newTitle);
        if (isSuccessful) {
            return "Название обновлено";
        } else {
            return "Книги не существует";
        }
    }

    @ShellMethod(value = "Удалиние книги", key = "del-book")
    public String deleteBookById(
            @ShellOption(help = "id книги для удаления") Long id) {
        boolean isSuccessful = libraryService.deleteBookById(id);
        if (isSuccessful) {
            return "Книга удалена";
        } else {
            return "Книги не существует";
        }
    }

    @ShellMethod(value = "Удалить автора", key = "del-auth")
    public String deleteAuthorById(
            @ShellOption(help = "id автора на удаление") Long id) {
        boolean isSuccessful = libraryService.deleteAuthorById(id);
        if (isSuccessful) {
            return "Автор удален";
        } else {
            return "Автора не существует";
        }
    }

    @ShellMethod(value = "Удалить жанр", key = "del-genre")
    public String deleteGenre(
            @ShellOption(help = "жанр на удаление") String genreName) {
        boolean isSuccessful = libraryService.deleteGenre(genreName);
        if (isSuccessful) {
            return "Жанр удален";
        } else {
            return "Жанра не существует";
        }
    }


    private String getTableFromList(List<Book> books) {
        TableModelBuilder<String> modelBuilder = new TableModelBuilder<>();
        modelBuilder.addRow()
                .addValue("id ")
                .addValue("Автор")
                .addValue("Название")
                .addValue("Жанр");
        books.forEach(book -> {
            Optional<String> authors = book.getAuthors().stream().map(Author::getName).reduce((a, b) -> a + ", " + b);
            modelBuilder.addRow()
                    .addValue((book.getId())+" ")
                    .addValue(authors.orElse("автор не указан")+" ")
                    .addValue(book.getTitle()+" ")
                    .addValue(book.getGenre().getGenreName()+" ");
        });
        TableModel model = modelBuilder.build();

        return new TableBuilder(model)
                .on(table())
                .addSizer(new AutoSizeConstraints())
                .addAligner(SimpleHorizontalAligner.left)
                .build()
                .render(400);
    }



}
