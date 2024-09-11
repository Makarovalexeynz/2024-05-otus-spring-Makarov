package ru.makarov.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "books")
@EqualsAndHashCode(of = {"id", "title", "author", "genre"})
public class Book {

    @Id
    private String id;

    private String title;

    private Author author;

    private Genre genre;

}
