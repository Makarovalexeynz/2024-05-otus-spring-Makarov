package ru.makarov.services;

import ru.makarov.models.Comment;

import java.util.Optional;

public interface CommentService {

    Optional<Comment> findById(long id);

    Comment insert(String text, long bookId);

    Comment update(long id, String text);

    void deleteById(long id);
}
