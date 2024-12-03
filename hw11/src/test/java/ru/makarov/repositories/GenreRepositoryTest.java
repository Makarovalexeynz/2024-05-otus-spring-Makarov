package ru.makarov.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.makarov.models.Genre;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Репозитой для работы с жанрами с монгой")
@DataMongoTest
public class GenreRepositoryTest {
    @Autowired
    private GenreRepository genreRepository;

    @DisplayName("должен загружать список всех жарнов")
    @Test
    void shouldReturnCorrectGenresList() {

        Flux<Genre> genres = genreRepository.findAll();
        List<Genre> genreList = genres.collectList().block();
        assertEquals(3, genreList.size());
    }

    @DisplayName("должен загружать жанр по ИД")
    @Test
    void shouldFindById() {

        Genre genre = new Genre("1", "Genre_1");

        Mono<Genre> foundGenre = genreRepository.findById("1");
        Genre found = foundGenre.block();

        assertNotNull(found);
        assertEquals(genre.getName(), found.getName());
    }
}
