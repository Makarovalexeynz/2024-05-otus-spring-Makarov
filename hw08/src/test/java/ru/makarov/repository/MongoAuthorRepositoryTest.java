package ru.makarov.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.makarov.models.Author;
import ru.makarov.repositories.AuthorRepository;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с авторами c mongodb")
@DataMongoTest
public class MongoAuthorRepositoryTest {

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = mongoTemplate.findAll(Author.class);
    }

    @DisplayName("должен загружать список всех авторов")
        @Test
        void shouldReturnCorrectAuthorsList() {
            var expectedAuthors = dbAuthors;
            var actualAuthors = repository.findAll();
            assertThat(actualAuthors)
                    .isNotNull()
                    .isNotEmpty()
                    .isEqualTo(expectedAuthors);
        }

        @DisplayName("должен загружать автора по id")
        @Test
        void shouldReturnCorrectAuthorById() {
            var expectedAuthor = new Author("2", "Author_2");
            var actualAuthor = repository.findById("2");
            assertThat(actualAuthor)
                    .isPresent()
                    .get()
                    .isEqualTo(expectedAuthor);
        }
}




