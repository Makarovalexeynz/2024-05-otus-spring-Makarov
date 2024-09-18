package ru.makarov.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.makarov.models.Genre;
import java.util.List;
import java.util.Optional;

public interface GenreRepository extends MongoRepository<Genre, String> {
    List<Genre> findAll();

    Optional<Genre> findById(String id);
}
