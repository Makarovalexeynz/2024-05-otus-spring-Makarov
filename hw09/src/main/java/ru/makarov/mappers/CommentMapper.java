package ru.makarov.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.makarov.dto.CommentDto;
import ru.makarov.models.Comment;

@Component
@AllArgsConstructor
public class CommentMapper {

    private final BookMapper bookMapper;

    public CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
        bookMapper.toDto(comment.getBook()));
    }
}
