package ru.makarov.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.makarov.dto.AuthorDto;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тесты авторконтроллера")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("тест вывода всех авторов")
    public void ShouldShowAllAuthors(){

        webTestClient.get().uri("/api/v1/authors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(AuthorDto.class)
                .consumeWith(response -> {
                    List<AuthorDto> authors = response.getResponseBody();
                    List<String> authorFullNames = authors.stream()
                            .map(AuthorDto::getFullName)
                            .collect(Collectors.toList());
                    assertThat(authorFullNames).containsExactlyInAnyOrder("Author_1", "Author_2", "Author_3");
                });
    }
}



