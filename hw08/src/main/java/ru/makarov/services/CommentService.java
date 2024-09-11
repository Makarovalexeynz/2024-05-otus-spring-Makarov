package ru.makarov.services;

import ru.makarov.models.Comment;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> findById(String id);

    Comment insert(String text, String bookId);

    Comment update(String id, String text);

    void deleteById(String id);
}
