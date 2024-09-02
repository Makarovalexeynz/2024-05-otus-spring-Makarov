package ru.makarov.service;

import org.springframework.context.annotation.Import;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.makarov.models.Book;
import ru.makarov.services.BookServiceImpl;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import({BookServiceImpl.class})
public class BookServiceTest {

    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnAllBooks() {
        var actualBooks = bookService.findAll();
        assertThat(actualBooks).isNotEmpty()
                .hasSize(3)
                .usingRecursiveComparison();
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnBookById() {

        Long bookId = 1L;
        Optional<Book> actualBook = bookService.findById(bookId);

        assertThat(actualBook).isPresent();

        Book book = actualBook.get();
        assertThat(book)
                .usingRecursiveComparison();
    }

    @DisplayName("должен сохранять книгу")
    @Test
    void shouldInsertBook() {
        String title = "New Book Title";
        long authorId = 1L;
        long genreId = 2L;

        Book insertedBook = bookService.insert(title, authorId, genreId);
        assertNotNull(insertedBook);

        assertThat(insertedBook).usingRecursiveComparison();

        var allBooks = bookService.findAll();
        assertThat(allBooks).hasSize(4);
        assertThat(allBooks).usingRecursiveComparison();
    }

    @DisplayName("Должен изменять книгу")
    @Test
    void shouldUpdateBook(){

        Long bookId = 1L;
        String updatedTitle = "Updated Book Title";
        long updatedAuthorId = 1L;
        long updatedGenreId = 2L;

        Optional<Book> optionalBook = bookService.findById(bookId);
        assertThat(optionalBook).isPresent();

        Book updatedBook = bookService.update(bookId, updatedTitle, updatedAuthorId, updatedGenreId);

        assertNotNull(updatedBook);

        assertThat(optionalBook)
                .usingRecursiveComparison();
    }

    @DisplayName("Должен удалять книгу")
    @Test
    void shouldDeleteBook(){
        Long bookIdToDelete = 1L;

        Optional<Book> existingBook = bookService.findById(bookIdToDelete);
        assertThat(existingBook).isPresent();

        bookService.deleteById(bookIdToDelete);

        Optional<Book> deletedBook = bookService.findById(bookIdToDelete);
        assertThat(deletedBook).isNotPresent()
                .usingRecursiveComparison();

        var remainingBooks = bookService.findAll();
        assertThat(remainingBooks).hasSize(2)
                .usingRecursiveComparison();
    }
}
