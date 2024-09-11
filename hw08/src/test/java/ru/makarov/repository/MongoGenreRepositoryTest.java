package ru.makarov.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.makarov.models.Genre;
import ru.makarov.repositories.GenreRepository;
import java.util.List;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с Genre")
@DataMongoTest

public class MongoGenreRepositoryTest {

    @Autowired
    private GenreRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = mongoTemplate.findAll(Genre.class);
    }

    @DisplayName("должен загружать список всех Genre")
    @Test
    void shouldReturnCorrectGenresList() {
        var expectedGenres = dbGenres;
        var actualGenres = repository.findAll();
        assertThat(actualGenres)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedGenres);
    }

    @DisplayName("должен загружать Genre по id")
    @Test
    void shouldReturnCorrectGenreById() {
        var expectedGenre = new Genre("2", "Genre_2");
        var actualGenre = repository.findById("2");
        assertThat(actualGenre)
                .isPresent()
                .get()
                .isEqualTo(expectedGenre);
    }

    private List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> mongoTemplate.findById(String.valueOf(id), Genre.class))
                .toList();
    }
}