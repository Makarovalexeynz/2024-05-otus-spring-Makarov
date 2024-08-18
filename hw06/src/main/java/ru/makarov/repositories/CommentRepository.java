package ru.makarov.repositories;

import ru.makarov.models.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Optional<Comment> findById(long id);

    Comment save(Comment comment);

    void deleteById(long id);

    List<Comment> findByBookId(long bookId);
}
