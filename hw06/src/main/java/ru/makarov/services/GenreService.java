package ru.makarov.services;

import ru.makarov.models.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();
}
