package ru.makarov.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.annotation.Import;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.makarov.models.Author;
import ru.makarov.models.Book;
import ru.makarov.models.Genre;
import ru.makarov.services.BookServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import({BookServiceImpl.class})
public class BookServiceTest {

    @Autowired
    private BookServiceImpl bookService;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
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
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnBookById(Book expectedBook) {
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
        long authorId = 1L;
        long genreId = 2L;

        Book expectedBook = new Book(0, title,
                new Author(authorId, "Author_" + authorId),
                new Genre(genreId, "Genre_" + genreId));

        Book insertedBook = bookService.insert(title, authorId, genreId);

        assertNotNull(insertedBook);
        assertThat(insertedBook.getId()).isGreaterThan(0);

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

        Long bookId = 1L;
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
        Long bookIdToDelete = 1L;

        Optional<Book> existingBook = bookService.findById(bookIdToDelete);
        assertThat(existingBook).isPresent();

        Book existingBookValue = existingBook.get();

        bookService.deleteById(bookIdToDelete);

        Optional<Book> deletedBook = bookService.findById(bookIdToDelete);
        assertThat(deletedBook).isNotPresent();

        assertThat(bookService.findById(bookIdToDelete)).isEmpty();

        var remainingBooks = bookService.findAll();
        assertThat(remainingBooks).hasSize(2);

        assertThat(remainingBooks).doesNotContain(existingBookValue);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id, "BookTitle_" + id, dbAuthors.get(id - 1), dbGenres.get(id - 1)))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }


}
