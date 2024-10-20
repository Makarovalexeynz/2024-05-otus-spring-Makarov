package ru.makarov.services;

import ru.makarov.dto.CommentDto;

public interface CommentService {

    CommentDto findById(long id);

    CommentDto insert(String text, long bookId);

    CommentDto update(long id, String text);

    void deleteById(long id);
}
