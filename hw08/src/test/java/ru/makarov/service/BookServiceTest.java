package ru.makarov.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.makarov.listeners.BookCascadeDeleteEventListener;
import ru.makarov.models.Author;
import ru.makarov.models.Book;
import ru.makarov.models.Comment;
import ru.makarov.models.Genre;
import ru.makarov.repositories.CommentRepository;
import ru.makarov.services.BookServiceImpl;
import ru.makarov.services.CommentServiceImpl;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@Import({BookServiceImpl.class, CommentServiceImpl.class, BookCascadeDeleteEventListener.class})
public class BookServiceTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentServiceImpl commentService;

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
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldInsertBook() {
        String title = "New Book Title";
        String authorId = "1";
        String genreId = "2";

        Book expectedBook = new Book("0", title,
                new Author(authorId, "Author_" + authorId),
                new Genre(genreId, "Genre_" + genreId));

        Book insertedBook = bookService.create(title, authorId, genreId);

        assertNotNull(insertedBook);

        var allBooks = bookService.findAll();
        assertThat(allBooks).hasSize(4);

        assertThat(allBooks).extracting(Book::getId).contains(insertedBook.getId());
        assertThat(allBooks).extracting(Book::getTitle).contains(insertedBook.getTitle());
        assertThat(allBooks).extracting(Book::getAuthor).contains(insertedBook.getAuthor());
        assertThat(allBooks).extracting(Book::getGenre).contains(insertedBook.getGenre());

        Optional<Book> foundBook = bookService.findById(insertedBook.getId());
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get()).usingRecursiveComparison().isEqualTo(insertedBook);
    }

    @DisplayName("Должен изменять книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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
        var allBooks = bookService.findAll();

        assertThat(allBooks).extracting(Book::getId).contains(updatedBook.getId());
        assertThat(allBooks).extracting(Book::getTitle).contains(updatedBook.getTitle());
        assertThat(allBooks).extracting(Book::getAuthor).contains(updatedBook.getAuthor());
        assertThat(allBooks).extracting(Book::getGenre).contains(updatedBook.getGenre());

        assertThat(updatedBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Должен удалять книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteBook(){
        String bookIdToDelete = "1";

        List <Comment> comments = commentRepository.findByBookId(bookIdToDelete);
        assertThat(comments).hasSize(1);

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

        List <Comment> commentsAfterBookDelete = commentRepository.findByBookId("bookIdToDelete");
          assertThat(commentsAfterBookDelete).isEmpty();
    }
}
