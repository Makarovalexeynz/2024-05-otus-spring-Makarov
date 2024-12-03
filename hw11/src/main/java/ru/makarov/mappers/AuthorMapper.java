package ru.makarov.mappers;

import org.springframework.stereotype.Component;
import ru.makarov.dto.AuthorDto;
import ru.makarov.models.Author;

@Component
public class AuthorMapper {

    public Author toModel(AuthorDto authorDto) {
        return new Author(authorDto.getId(), authorDto.getFullName());
    }

    public AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }
}
