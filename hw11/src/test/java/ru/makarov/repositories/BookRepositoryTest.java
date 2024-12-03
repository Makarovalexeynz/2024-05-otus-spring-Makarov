package ru.makarov.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.makarov.models.Author;
import ru.makarov.models.Book;
import ru.makarov.models.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Тесты букрепозитория")
@DataMongoTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("Должен загружать все книги")
    @Test
    void shouldFindAll() {

        Flux<Book> books = bookRepository.findAll();

        List<Book> bookList = books.collectList().block();

        assertEquals(3, bookList.size());
    }

    @DisplayName("Должен искать книгу по ИД")
    @Test
    void shouldFindById() {

        Book book = new Book("1","BookTitle_1",
                new Author("1", "Author_1"),
                new Genre("1", "Genre_1"));

        Mono<Book> foundBook = bookRepository.findById("1");
        Book found = foundBook.block();
        assertNotNull(found);
        assertEquals(book.getTitle(), found.getTitle());
    }
    @DisplayName("поиск книги по несуществующему ИД")
    @Test
    void shouldFindByIdNotFound() {

        Mono<Book> foundBook = bookRepository.findById("50");

        assertNull(foundBook.block());
    }

    @DisplayName("Тест на сохранение книги")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveBook() {

        Book bookForSave = new Book(null,
                "Test Title",
                new Author("1", "Test Author"),
                new Genre("1", "Test Genre"));

        Mono<Book> savedBook = bookRepository.save(bookForSave);

        Book saved = savedBook.block();
        assertNotNull(saved);
        assertNotNull(saved.getId());
    }
}
