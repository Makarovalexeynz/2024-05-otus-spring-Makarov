package ru.makarov.services;

import ru.makarov.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
