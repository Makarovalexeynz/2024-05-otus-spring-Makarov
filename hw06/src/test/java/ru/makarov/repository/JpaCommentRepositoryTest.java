package ru.makarov.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.makarov.models.Author;
import ru.makarov.models.Book;
import ru.makarov.models.Comment;
import ru.makarov.models.Genre;
import ru.makarov.repositories.JpaCommentRepository;
import java.util.List;
import java.util.stream.IntStream;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями")
@DataJpaTest
@Import(JpaCommentRepository.class)
public class JpaCommentRepositoryTest {

    @Autowired
    private JpaCommentRepository repositoryJpa;

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getDbComments")
    void shouldReturnCorrectCommentById(Comment expectedComment) {
        var actualComment = repositoryJpa.findById(expectedComment.getId());
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 6).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id, "BookTitle_" + id, dbAuthors.get(id - 1), dbGenres.get(id - 1)))
                .toList();
    }

    private static List<Comment> getDbComments(List<Book> books) {
        return List.of(new Comment(1, "Comment_1", books.get(0)),
                new Comment(2, "Comment_2", books.get(0)),
                new Comment(3, "Comment_3", books.get(0)),
                new Comment(4, "Comment_4", books.get(1)),
                new Comment(5, "Comment_5", books.get(1)),
                new Comment(6, "Comment_6", books.get(2))
        );
    }
    private static List<Comment> getDbComments() {
        var books = getDbBooks(getDbAuthors(), getDbGenres());
        return getDbComments(books);
    }
}
