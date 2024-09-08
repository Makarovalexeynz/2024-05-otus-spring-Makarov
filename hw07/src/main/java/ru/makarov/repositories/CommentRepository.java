package ru.makarov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.makarov.models.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(long id);


    Comment save(Comment comment);

    void deleteById(long id);

    List<Comment> findByBookId(long bookId);
}
