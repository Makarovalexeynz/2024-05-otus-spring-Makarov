package ru.makarov.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.makarov.models.Author;
import ru.makarov.models.Book;
import ru.makarov.models.Comment;
import ru.makarov.models.Genre;
import ru.makarov.services.BookServiceImpl;
import ru.makarov.services.CommentServiceImpl;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@Import({BookServiceImpl.class, CommentServiceImpl.class, })
public class CommentServiceTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("Должен находить комментарий по Id")
    @Test
    void shouldReturnCommentById(){

        String commentId = "2";

        Comment expectedComment = new Comment(commentId, "CommentText_2",
                new Book("2", "BookTitle_2",
                        new Author("2", "Author_2"),
                        new Genre("2", "Genre_2")));

        Optional <Comment> actualComment = commentService.findById(commentId);

        assertThat(actualComment).isPresent();

        assertThat(actualComment.get())
                .isEqualTo(expectedComment);
    }

    @DisplayName("Должен создавать новый комментарий")
    @Test
    void shouldInsertNewComment(){
        String text = "New Comment";
        String bookId = "1";

        Comment insertedComment = commentService.insert(text, bookId);
        assertNotNull(insertedComment);
        assertThat(insertedComment.getText()).isEqualTo(text);
        assertThat(insertedComment.getBook().getId()).isEqualTo(bookId);
    }

    @DisplayName("Должен изменять комментарий")
    @Test
    void shouldUpdateComment(){

        String commentId = "1";
        String updatedText = "Updated text";
        String updatedBookId = "1";

        Optional<Comment> optionalComment = commentService.findById(commentId);
        assertThat(optionalComment).isPresent();

        Comment updatedComment = commentService.update(commentId, updatedText);

        assertNotNull(updatedComment);

        assertThat(updatedComment.getId()).isEqualTo(commentId);
        assertThat(updatedComment.getText()).isEqualTo(updatedText);
    }

    @DisplayName("Должен удалять комментарий")
    @Test
    void shouldDeleteComment(){
        String commentIdToDelete = "1";

        Optional<Comment> existingComment = commentService.findById(commentIdToDelete);
        assertThat(existingComment).isPresent();

        commentService.deleteById(commentIdToDelete);

        Optional<Comment> deletedComment = commentService.findById(commentIdToDelete);
        assertThat(deletedComment).isNotPresent();
    }
}
