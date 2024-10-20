package ru.makarov.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.makarov.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph("book-author-genre")
    Optional<Book> findById(long id);

    @EntityGraph("book-author-genre")
    @Override
    List<Book> findAll();
}
