package ru.makarov.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.makarov.dto.CommentDto;
import ru.makarov.exceptions.NotFoundException;
import ru.makarov.mappers.AuthorMapper;
import ru.makarov.mappers.BookMapper;
import ru.makarov.mappers.CommentMapper;
import ru.makarov.mappers.GenreMapper;
import ru.makarov.models.Author;
import ru.makarov.models.Book;
import ru.makarov.models.Comment;
import ru.makarov.models.Genre;
import ru.makarov.services.BookServiceImpl;
import ru.makarov.services.CommentServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({BookServiceImpl.class, CommentServiceImpl.class, BookMapper.class, AuthorMapper.class, GenreMapper.class, CommentMapper.class  })
public class CommentServiceTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private GenreMapper genreMapper;

    @Autowired
    private BookMapper bookMapper;


    @DisplayName("Должен находить комментарий по Id")
    @Test
    void shouldReturnCommentById(){

        Long commentId = 1L;

        Comment expectedComment = new Comment(commentId, "comment_1",
                new Book(1, "BookTitle_1",
                        new Author(1, "Author_1"),
                        new Genre(1, "Genre_1")));

        CommentDto expectedCommentDto = commentMapper.toDto(expectedComment);

        CommentDto actualCommentDto = commentService.findById(commentId);

        assertThat(actualCommentDto)
                .isEqualTo(expectedCommentDto);
    }

    @DisplayName("Должен создавать новый комментарий")
    @Test
    void shouldInsertNewComment(){
        String text = "New Comment";
        long bookId = 1L;

        CommentDto insertedComment = commentService.insert(text, bookId);
        assertNotNull(insertedComment);
        assertThat(insertedComment.getText()).isEqualTo(text);
        assertThat(insertedComment.getBook().getId()).isEqualTo(bookId);
    }

    @DisplayName("Должен изменять комментарий")
    @Test
    void shouldUpdateComment(){

        Long commentId = 1L;
        String updatedText = "Updated text";
        long updatedBookId = 1L;

        CommentDto optionalComment = commentService.findById(commentId);

        CommentDto updatedComment = commentService.update(commentId, updatedText);

        assertNotNull(updatedComment);

        assertThat(updatedComment.getId()).isEqualTo(commentId);
        assertThat(updatedComment.getText()).isEqualTo(updatedText);
    }

    @DisplayName("Должен удалять комментарий")
    @Test
    void shouldDeleteComment(){
        Long commentIdToDelete = 1L;

        CommentDto comment = commentService.findById(commentIdToDelete);
        assertNotNull(comment, "Комментарий с ID " + commentIdToDelete + " должен существовать.");

        commentService.deleteById(commentIdToDelete);
        assertThrows(NotFoundException.class, () -> commentService.findById(commentIdToDelete));







    }
}
