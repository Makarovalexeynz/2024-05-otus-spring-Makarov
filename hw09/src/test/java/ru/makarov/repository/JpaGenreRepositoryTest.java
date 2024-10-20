package ru.makarov.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.makarov.models.Genre;
import ru.makarov.repositories.GenreRepository;
import java.util.List;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с Genre")
@DataJpaTest

public class JpaGenreRepositoryTest {

    @Autowired
    private GenreRepository repositoryJpa;

    @DisplayName("должен загружать список всех Genre")
    @Test
    void shouldReturnCorrectGenresList() {
        var expectedGenres = getDbGenres();
        var actualGenres = repositoryJpa.findAll();
        assertThat(actualGenres)
                .isNotNull()
                .isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenres);
    }

    @DisplayName("должен загружать Genre по id")
    @Test
    void shouldReturnCorrectGenreById() {
        var expectedGenre = new Genre(2, "Genre_2");
        var actualGenre = repositoryJpa.findById(2L);
        assertThat(actualGenre)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenre);
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }
}