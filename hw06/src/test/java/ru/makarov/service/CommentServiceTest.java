package ru.makarov.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.makarov.models.Comment;
import ru.makarov.repositories.JpaAuthorRepository;
import ru.makarov.repositories.JpaBookRepository;
import ru.makarov.repositories.JpaCommentRepository;
import ru.makarov.repositories.JpaGenreRepository;
import ru.makarov.services.BookServiceImpl;
import ru.makarov.services.CommentServiceImpl;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import({BookServiceImpl.class, JpaBookRepository.class, JpaAuthorRepository.class, JpaGenreRepository.class, CommentServiceImpl.class, JpaCommentRepository.class})
public class CommentServiceTest {

    @Autowired
    private CommentServiceImpl commentService;

    @DisplayName("Должен находить комментарий по Id")
    @Test
    void shouldReturnCommentById(){

        Long commentId = 1L;
        Optional<Comment> actualComment = commentService.findById(commentId);

        assertThat(actualComment).isPresent();

        Comment comment = actualComment.get();
        assertThat(comment)
                .hasFieldOrPropertyWithValue("text", "Comment_1");
    }

    @DisplayName("Должен создавать новый комментарий")
    @Test
    void shouldInsertNewComment(){
        String text = "New Comment";
        long bookId = 1L;

        Comment insertedComment = commentService.insert(text, bookId);
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

        Optional<Comment> optionalComment = commentService.findById(commentId);
        assertThat(optionalComment).isPresent();

        Comment updatedComment = commentService.update(commentId, updatedText, updatedBookId);

        assertNotNull(updatedComment);

        assertThat(updatedComment.getId()).isEqualTo(commentId);
        assertThat(updatedComment.getText()).isEqualTo(updatedText);
        assertThat(updatedComment.getBook().getId()).isEqualTo(updatedBookId);
    }

    @DisplayName("Должен удалять комментарий")
    @Test
    void shouldDeleteComment(){
        Long commentIdToDelete = 1L;

        Optional<Comment> existingComment = commentService.findById(commentIdToDelete);
        assertThat(existingComment).isPresent();

        commentService.deleteById(commentIdToDelete);

        Optional<Comment> deletedComment = commentService.findById(commentIdToDelete);
        assertThat(deletedComment).isNotPresent();
    }
}
