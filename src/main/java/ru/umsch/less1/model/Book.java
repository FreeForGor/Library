package ru.umsch.less1.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Book {

    private Long id;
    private List<Author> authors;
    private String title;
    private Genre genre;

    public Book(String title, Genre genre, List<Author> authorList) {

        this.authors = authorList;
        this.genre = genre;
        this.title = title;
    }
}
