package ru.makarov.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.makarov.models.GenreMongo;

import java.util.List;
import java.util.Optional;

public interface GenreRepositoryMongo extends MongoRepository<GenreMongo, String> {

    List<GenreMongo> findAll();

    Optional<GenreMongo> findById(String id);

}
