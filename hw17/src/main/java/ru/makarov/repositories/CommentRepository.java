package ru.makarov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.makarov.models.Comment;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBookId(long id);
}
