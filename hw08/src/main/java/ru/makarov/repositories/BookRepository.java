package ru.makarov.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.makarov.models.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    Optional<Book> findById(String id);

    List<Book> findAll();

    Book save(Book book);

    void deleteById(String id);
}
