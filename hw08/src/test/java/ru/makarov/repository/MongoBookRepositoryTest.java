package ru.makarov.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.makarov.listeners.BookCascadeDeleteEventListener;
import ru.makarov.models.Author;
import ru.makarov.models.Book;
import ru.makarov.models.Genre;
import ru.makarov.repositories.BookRepository;
import ru.makarov.repositories.CommentRepository;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Репозиторий для работы с книгами ")
@DataMongoTest
@Import({BookCascadeDeleteEventListener.class})
class MongoBookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = mongoTemplate.findAll(Author.class);
        dbGenres = mongoTemplate.findAll(Genre.class);
        dbBooks = mongoTemplate.findAll(Book.class);
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var expectedBook = dbBooks.get(0);
        var actualBook = repository.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repository.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks)
                .isNotNull()
                .isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(expectedBooks);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var author = new Author("0", "Test Author");
        var genre = new Genre("0", "Test Genre");
        var expectedBook = new Book("0", "BookTitle_10500", author, genre);
        var returnedBook = repository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null)
                .usingRecursiveComparison().isEqualTo(expectedBook);

        assertThat(repository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book("1", "BookTitle_10500", dbAuthors.get(2), dbGenres.get(2));

        assertThat(repository.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isNotEqualTo(expectedBook);

        var returnedBook = repository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null)
                .usingRecursiveComparison().isEqualTo(expectedBook);

        assertThat(repository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(repository.findById("1")).isPresent();
        repository.deleteById("1");
        assertThat(repository.findById("1")).isEmpty();
        assertThat(commentRepository.findByBookId("1")).isEmpty();
    }
}