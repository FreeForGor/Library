package ru.umsch.less1.model;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor


public class Genre {

    private Long id;
    private String genreName;

    public Genre(String genre) {
        this.genreName = genre;
    }

}
