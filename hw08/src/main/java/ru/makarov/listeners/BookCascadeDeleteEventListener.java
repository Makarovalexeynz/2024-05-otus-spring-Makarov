package ru.makarov.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.makarov.models.Book;
import ru.makarov.repositories.CommentRepository;

@Component
@RequiredArgsConstructor
public class BookCascadeDeleteEventListener extends AbstractMongoEventListener<Book> {

    private final CommentRepository commentRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        super.onBeforeDelete(event);
        String bookId = String.valueOf(event.getSource().get("_id"));
        commentRepository.deleteAllByBookId(bookId);
    }
}