package ru.makarov.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.makarov.dto.GenreDto;
import ru.makarov.mappers.GenreMapper;
import ru.makarov.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @GetMapping("/api/v1/genres")
    public Flux<GenreDto> getAllGenres() {

        return genreRepository.findAll().map(genreMapper::toDto);
    }
}
