package ru.makarov.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.makarov.models.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, String> {

    Optional<Comment> findById(String id);

    Comment save(Comment comment);

    void deleteById(String id);

    @Query("{ 'book.id': ?0 }")
    List<Comment> findByBookId(String bookId);

    @Query(value = "{ 'book.id': ?0 }", delete = true)
    void deleteAllByBookId(String bookId);


}
