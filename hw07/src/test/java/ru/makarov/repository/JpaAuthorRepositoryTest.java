package ru.makarov.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;
import ru.makarov.models.Author;
import ru.makarov.repositories.AuthorRepository;
import java.util.stream.IntStream;

import java.util.List;

@DisplayName("Репозиторий на основе Jpa для работы с авторами")
@DataJpaTest
public class JpaAuthorRepositoryTest {

    @Autowired
    private AuthorRepository repositoryJpa;

        @DisplayName("должен загружать список всех авторов")
        @Test
        void shouldReturnCorrectAuthorsList() {
            var expectedAuthors = getDbAuthors();
            var actualAuthors = repositoryJpa.findAll();
            assertThat(actualAuthors)
                    .isNotNull()
                    .isNotEmpty()
                    .isEqualTo(expectedAuthors);
        }

        @DisplayName("должен загружать автора по id")
        @Test
        void shouldReturnCorrectAuthorById() {
            var expectedAuthor = new Author(2, "Author_2");
            var actualAuthor = repositoryJpa.findById(2);
            assertThat(actualAuthor)
                    .isPresent()
                    .get()
                    .isEqualTo(expectedAuthor);
        }

        private static List<Author> getDbAuthors() {
            return IntStream.range(1, 4).boxed()
                    .map(id -> new Author(id, "Author_" + id))
                    .toList();
        }
}




