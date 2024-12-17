package ru.makarov.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.makarov.models.BookMongo;

import java.util.List;
import java.util.Optional;

public interface BookRepositoryMongo extends MongoRepository<BookMongo, String> {

    Optional<BookMongo> findById(String id);

    List<BookMongo> findAll();

    BookMongo save(BookMongo book);

    void deleteById(String id);
}
