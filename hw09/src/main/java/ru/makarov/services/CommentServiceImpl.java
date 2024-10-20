package ru.makarov.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.makarov.dto.CommentDto;
import ru.makarov.exceptions.NotFoundException;
import ru.makarov.mappers.CommentMapper;
import ru.makarov.models.Book;
import ru.makarov.models.Comment;
import ru.makarov.repositories.BookRepository;
import ru.makarov.repositories.CommentRepository;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Override
    public CommentDto findById(long id) {
        var findByIdComment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment not found"));

        return commentMapper.toDto(findByIdComment);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto insert(String text, long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Книга с ID " + bookId + " не найдена."));
        Comment comment = new Comment(0, text, book);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto update(long id, String text) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий с ID " + id + " не найден."));
        comment.setText(text);

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
