package ru.makarov.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.makarov.models.AuthorMongo;
import java.util.List;
import java.util.Optional;

public interface AuthorRepositoryMongo extends MongoRepository<AuthorMongo, String> {

    List<AuthorMongo> findAll();

    Optional<AuthorMongo> findById(String id);
}
