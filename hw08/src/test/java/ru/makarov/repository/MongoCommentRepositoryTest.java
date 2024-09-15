package ru.makarov.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.makarov.models.Author;
import ru.makarov.models.Book;
import ru.makarov.models.Comment;
import ru.makarov.models.Genre;
import ru.makarov.repositories.CommentRepository;
import java.util.List;

@DisplayName("Репозиторий для работы с комментариями")
@DataMongoTest
public class MongoCommentRepositoryTest {

    @Autowired
    private CommentRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    private List<Comment> dbComments;

    @BeforeEach
    void setUp() {
        dbAuthors = mongoTemplate.findAll(Author.class);
        dbGenres = mongoTemplate.findAll(Genre.class);
        dbBooks = mongoTemplate.findAll(Book.class);
        dbComments = mongoTemplate.findAll(Comment.class);
    }

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var expectedComment = dbComments.get(0);
        var actualComment = repository.findById(expectedComment.getId());
    }
}
