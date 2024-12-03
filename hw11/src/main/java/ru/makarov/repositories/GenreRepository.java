package ru.makarov.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.makarov.models.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {

    Flux<Genre> findAll();

    Mono<Genre> findById(String id);
}
