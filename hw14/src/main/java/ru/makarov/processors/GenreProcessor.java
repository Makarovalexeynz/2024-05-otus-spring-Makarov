package ru.makarov.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.stereotype.Component;
import ru.makarov.models.Genre;
import ru.makarov.models.GenreMongo;

@Component
public class GenreProcessor implements ItemProcessor<GenreMongo, Genre> {
    @Override
    public Genre process(GenreMongo item) throws Exception {
        if (item == null || item.getId() == null || item.getName() == null) {
            throw new ItemStreamException("Invalid GenreMongo data: " + item);
        }
        Genre genre = new Genre();
        genre.setId(Long.parseLong(item.getId()));
        genre.setName(item.getName());
        return genre;
    }
}
