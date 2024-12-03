package ru.makarov.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.makarov.dto.AuthorDto;
import ru.makarov.mappers.AuthorMapper;
import ru.makarov.repositories.AuthorRepository;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;

    private final  AuthorMapper authorMapper;

    @GetMapping("/api/v1/authors")
    public Flux<AuthorDto> getListAuthors() {
        return authorRepository.findAll().map(authorMapper::toDto);
    }
}
