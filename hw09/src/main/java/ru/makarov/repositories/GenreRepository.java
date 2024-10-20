package ru.makarov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.makarov.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> { }
