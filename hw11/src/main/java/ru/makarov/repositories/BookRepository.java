package ru.makarov.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.makarov.models.Book;


public interface BookRepository extends ReactiveMongoRepository<Book, String> {

    Mono<Book> findById(String id);

    Flux<Book> findAll();

    Mono<Book> save(Book book);
}