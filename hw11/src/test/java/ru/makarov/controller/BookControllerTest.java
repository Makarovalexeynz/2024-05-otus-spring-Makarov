package ru.makarov.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.makarov.dto.BookCreateDto;
import ru.makarov.dto.BookDto;
import ru.makarov.dto.BookUpdateDto;
import ru.makarov.models.Book;
import java.util.List;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("тесты для контролера книг")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private int port;


    @DisplayName("должен возвращать все книги")
    @Test
    public void shouldShowAllBooks() {

        webTestClient.get().uri("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BookDto.class)
                .consumeWith(response -> {
                    List<BookDto> books = response.getResponseBody();
                    List<String> booksTitle = books.stream()
                            .map(BookDto::getTitle)
                            .collect(Collectors.toList());
                    assertThat(booksTitle).containsExactlyInAnyOrder("BookTitle_1", "BookTitle_2", "BookTitle_3");
                });
    }

    @DisplayName("должен возвращать  книгу по ИД")
    @Test
    public void shouldShowBookById() {

        webTestClient.get().uri("/api/v1/books/2")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BookDto.class)
                .consumeWith(response -> {
                    List<BookDto> books = response.getResponseBody();
                    List<String> booksTitle = books.stream()
                            .map(BookDto::getTitle)
                            .collect(Collectors.toList());
                    assertThat(booksTitle).containsExactlyInAnyOrder("BookTitle_2");
                });
    }
    @DisplayName("Должен сохранять книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)

    public void shuoldCreateBook() {

        BookCreateDto bookCreateDto = new BookCreateDto( "Test Book Title", "1", "1");


        webTestClient.post()
                .uri("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(bookCreateDto) ,BookCreateDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .consumeWith(response -> {
                    Book savedBook = response.getResponseBody();
                    assertThat(savedBook.getTitle()).isEqualTo("Test Book Title");
                });
    }
    @DisplayName("Должен обновлять книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)

    public void shouldUpdateBook() {

        BookUpdateDto bookUpdateDto = new BookUpdateDto("1","New Title", "1", "1");

        webTestClient.put()
                .uri("/api/v1/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(bookUpdateDto), BookUpdateDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .consumeWith(response -> {
                    Book updatedBook = response.getResponseBody();
                    assertThat(updatedBook.getTitle()).isEqualTo("New Title");
                });
    }
}
