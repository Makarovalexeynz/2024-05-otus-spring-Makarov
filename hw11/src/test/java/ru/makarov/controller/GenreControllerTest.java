package ru.makarov.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.makarov.dto.GenreDto;
import java.util.List;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тесты контроллера жанров")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GenreControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("тест вывода всех жанров")
    public void ShouldShowAllGenres(){

        webTestClient.get().uri("/api/v1/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(GenreDto.class)
                .consumeWith(response -> {
                    List<GenreDto> genres = response.getResponseBody();
                    List<String> genreNames = genres.stream()
                            .map(GenreDto::getName)
                            .collect(Collectors.toList());
                    assertThat(genreNames).containsExactlyInAnyOrder("Genre_1", "Genre_2", "Genre_3");
                });
    }
}
