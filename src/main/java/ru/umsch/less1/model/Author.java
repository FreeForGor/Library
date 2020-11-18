package ru.umsch.less1.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor


public class Author {

    private Long id;
    private List<Book> books;
    private String name;

    public Author(String authorName) {
        this.name = authorName;
    }

    public Author(List<Book> books, String name) {
        this.books = books;
        this.name = name;
    }
}
