package ru.makarov.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.makarov.models.AuthorMongo;
import ru.makarov.models.BookMongo;
import ru.makarov.models.GenreMongo;
import ru.makarov.repositories.AuthorRepositoryMongo;
import ru.makarov.repositories.BookRepositoryMongo;
import ru.makarov.repositories.GenreRepositoryMongo;
import java.util.Arrays;
import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseChangelog.class);

    private List<AuthorMongo> authors;

    private List<GenreMongo> genres;

    private List<BookMongo> books;


    @ChangeSet(order = "001", id = "dropDb", author = "Makarov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "Makarov")
    public void insertAuthors(AuthorRepositoryMongo authorRepository) {
        authors = Arrays.asList(new AuthorMongo("1","Author_1"),
                new AuthorMongo("2","Author_2"),
                new AuthorMongo("3","Author_3"));
        LOGGER.info("Начата миграция insertAuthors");
        try {
            long startTime = System.currentTimeMillis();
            authorRepository.saveAll(authors);
            LOGGER.info("Успешно добавлено {} авторов за {} мс", authors.size(),
                    System.currentTimeMillis() - startTime);

        } catch (Exception e) {
            LOGGER.error("Непредвиденная ошибка при добавлении авторов: {}", e.getMessage(), e);
        }
        LOGGER.info("Завершена миграция insertAuthors");
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "Makarov")
    public void insertGenres(GenreRepositoryMongo genreRepository) {
        genres = Arrays.asList(new GenreMongo("1","Genre_1"),
                new GenreMongo("2","Genre_2"),
                new GenreMongo("3","Genre_3")

        );
        LOGGER.info("Начали делать жанры");
        try {
            long startTime = System.currentTimeMillis();
            genreRepository.saveAll(genres);
            LOGGER.info("Успешно добавлено {} жанры за {} мс", genres.size(),
                    System.currentTimeMillis() - startTime);

        } catch (Exception e) {
            LOGGER.error("Непредвиденная ошибка при добавлении жанров: {}", e.getMessage(), e);
        }
        LOGGER.info("Завершена миграция insertGenres");
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "makarov")
    public void insertBooks(BookRepositoryMongo bookRepository) {
        books = Arrays.asList(
                new BookMongo("1","BookTitle_1", authors.get(0), genres.get(0)),
                new BookMongo("2","BookTitle_2", authors.get(1), genres.get(1)),
                new BookMongo("3", "BookTitle_3", authors.get(2), genres.get(2))
        );
         bookRepository.saveAll(books);
    }
}

