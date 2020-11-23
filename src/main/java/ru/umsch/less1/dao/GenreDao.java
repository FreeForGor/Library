package ru.umsch.less1.dao;

import ru.umsch.less1.model.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> getAllGenres();
    Genre getGenreByName(String genreName);

    Genre addGenre(Genre genre);

    void deleteGenre(Genre genre);
    int deleteAll();
}
