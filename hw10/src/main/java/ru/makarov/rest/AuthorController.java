package ru.makarov.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.makarov.dto.AuthorDto;
import ru.makarov.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/api/v1/authors")
    public List<AuthorDto> getListAuthors() {
        return authorService.findAll();
    }
}
