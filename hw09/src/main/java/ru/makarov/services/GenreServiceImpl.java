package ru.makarov.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.makarov.dto.GenreDto;
import ru.makarov.mappers.GenreMapper;
import ru.makarov.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream().map(genreMapper::toDto).toList();
    }
}
