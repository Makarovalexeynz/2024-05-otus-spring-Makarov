package ru.makarov.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.stereotype.Component;
import ru.makarov.dto.BookUpdateDto;
import ru.makarov.models.BookMongo;

@Component
public class BookProcessor implements ItemProcessor<BookMongo, BookUpdateDto> {
    @Override
    public BookUpdateDto process(BookMongo item) throws Exception {
        if (item == null || item.getId() == null || item.getTitle() == null) {
            throw new ItemStreamException("Invalid BookMongo data: " + item);
        }

        BookUpdateDto book = new BookUpdateDto();
        book.setId(Long.parseLong(item.getId()));
        book.setTitle(item.getTitle());
        book.setAuthorId(Long.parseLong(item.getAuthor().getId()));
        book.setGenreId(Long.parseLong(item.getGenre().getId()));

        return book;
    }
}
