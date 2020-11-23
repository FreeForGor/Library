package ru.umsch.less1.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "authors")
    private Set<Book> books;

    @Column(name = "author_name")
    private String name;

    public Author(String authorName) {
        this.name = authorName;
    }

    public Author(Set<Book> books, String name) {
        this.books = books;
        this.name = name;
    }

    public Set<Book> getBooks() {
        if (books == null) {
            return new HashSet<>();
        }
        return books;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getBooks() != null ? getBooks().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }
}
