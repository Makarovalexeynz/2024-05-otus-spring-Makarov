package ru.makarov.services;

import ru.makarov.dto.GenreDto;

import java.util.List;

public interface GenreService {
    List<GenreDto> findAll();
}
