package ru.makarov.mappers;

import org.springframework.stereotype.Component;
import ru.makarov.dto.GenreDto;
import ru.makarov.models.Genre;

@Component
public class GenreMapper {

    public Genre toModel(GenreDto genreDto) {
        return new Genre(genreDto.getId(), genreDto.getName());
    }

    public GenreDto toDto(Genre genre) {

        return new GenreDto(genre.getId(), genre.getName());
    }
}
