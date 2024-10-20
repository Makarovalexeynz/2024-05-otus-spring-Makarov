package ru.makarov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.makarov.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> { }
