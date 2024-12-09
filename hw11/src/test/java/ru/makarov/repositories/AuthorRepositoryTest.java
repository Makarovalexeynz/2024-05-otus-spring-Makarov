package ru.makarov.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.makarov.models.Author;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Репозиторий для работы с авторами c mongodb")
@DataMongoTest
public class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {

        Flux<Author> authors = authorRepository.findAll();
        List<Author> authorList = authors.collectList().block();
        assertEquals(3, authorList.size());
    }

    @DisplayName("должен загружать автора по ИД")
    @Test
    void shouldFindById() {

        Author author = new Author("1", "Author_1");

        Mono<Author> foundAuthor = authorRepository.findById("1");
        Author found = foundAuthor.block();

        assertNotNull(found);
        assertEquals(author.getFullName(), found.getFullName());
    }
}




