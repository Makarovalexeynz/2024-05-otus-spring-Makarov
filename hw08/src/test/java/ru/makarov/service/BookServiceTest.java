package ru.makarov.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.makarov.models.Author;
import ru.makarov.models.Book;
import ru.makarov.models.Genre;
import ru.makarov.services.BookServiceImpl;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@Import({BookServiceImpl.class})
public class BookServiceTest {

    @Autowired
    private BookServiceImpl bookService;

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

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnAllBooks() {

        var actualBooks = bookService.findAll();
        var expectedBooks = dbBooks;
        assertThat(actualBooks).isNotEmpty()
                .hasSize(3)
                .hasOnlyElementsOfType(Book.class)
                .usingRecursiveComparison()
                .isEqualTo(expectedBooks);
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnBookById() {
        var expectedBook = dbBooks.get(0);
        var actualBook = bookService.findById(expectedBook.getId());
        assertThat(actualBook)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять книгу")
    @Test
    void shouldInsertBook() {
        String title = "New Book Title";
        String authorId = "1";
        String genreId = "2";

        Book expectedBook = new Book("0", title,
                new Author(authorId, "Author_" + authorId),
                new Genre(genreId, "Genre_" + genreId));

        Book insertedBook = bookService.insert(title, authorId, genreId);

        assertNotNull(insertedBook);

        var allBooks = bookService.findAll();
        assertThat(allBooks).hasSize(4);

        assertThat(allBooks).contains(insertedBook);

        Optional<Book> foundBook = bookService.findById(insertedBook.getId());
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get()).usingRecursiveComparison().isEqualTo(insertedBook);
    }

    @DisplayName("Должен изменять книгу")
    @Test
    void shouldUpdateBook(){

        String bookId = "1";
        String updatedTitle = "Updated Book Title";
        Author updatedAuthor = dbAuthors.get(1);
        Genre updatedGenre = dbGenres.get(2);

        Book expectedBook = new Book(bookId,updatedTitle,
                new Author(updatedAuthor.getId(),updatedAuthor.getFullName()),
                new Genre(updatedGenre.getId(), updatedGenre.getName()));

        Book updatedBook = bookService.update(bookId, updatedTitle, updatedAuthor.getId(), updatedGenre.getId());
        assertNotNull(updatedBook);
        var allBook = bookService.findAll();
        assertThat(allBook).contains(updatedBook);
        assertThat(updatedBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Должен удалять книгу")
    @Test
    void shouldDeleteBook(){
        String bookIdToDelete = "1";

        Optional<Book> existingBook = bookService.findById(bookIdToDelete);
        assertThat(existingBook).isPresent();

        Book existingBookValue = existingBook.get();

        bookService.deleteById(bookIdToDelete);

        Optional<Book> deletedBook = bookService.findById(bookIdToDelete);
        assertThat(deletedBook).isNotPresent();

        assertThat(bookService.findById(bookIdToDelete)).isEmpty();

        var remainingBooks = bookService.findAll();
        assertThat(remainingBooks).hasSize(3);

        assertThat(remainingBooks).doesNotContain(existingBookValue);
    }
}
