package ru.makarov.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.stereotype.Component;
import ru.makarov.models.Author;
import ru.makarov.models.AuthorMongo;

@Component
public class AuthorProcessor implements ItemProcessor<AuthorMongo, Author> {
    @Override
    public Author process(AuthorMongo item) throws Exception {
        if (item == null || item.getId() == null || item.getFullName() == null) {
            throw new ItemStreamException("Invalid AuthorMongo data: " + item);
        }
        Author author = new Author();
        author.setId(Long.parseLong(item.getId()));
        author.setFullName(item.getFullName());
        return author;
    }
}
