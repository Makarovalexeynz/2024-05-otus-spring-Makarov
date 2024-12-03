package ru.makarov.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.makarov.models.Author;
import ru.makarov.models.Book;
import ru.makarov.models.Genre;
import ru.makarov.repositories.AuthorRepository;
import ru.makarov.repositories.BookRepository;
import ru.makarov.repositories.GenreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseChangelog.class);

    private List<Author> authors;

    private List<Genre> genres;

    private List<Book> books;


    @ChangeSet(order = "001", id = "dropDb", author = "Makarov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "Makarov")
    public void insertAuthors(AuthorRepository authorRepository) {
        authors = Arrays.asList(new Author("1","Author_1"),
                new Author("2","Author_2"),
                new Author("3","Author_3"));
        LOGGER.info("Начата миграция insertAuthors");
        try {
            long startTime = System.currentTimeMillis();
            authorRepository.saveAll(authors).blockLast();
            LOGGER.info("Успешно добавлено {} авторов за {} мс", authors.size(),
                    System.currentTimeMillis() - startTime);

        } catch (Exception e) {
            LOGGER.error("Непредвиденная ошибка при добавлении авторов: {}", e.getMessage(), e);
        }
        LOGGER.info("Завершена миграция insertAuthors");
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "Makarov")
    public void insertGenres(GenreRepository genreRepository) {
        genres = Arrays.asList(new Genre("1","Genre_1"),
                new Genre("2","Genre_2"),
                new Genre("3","Genre_3")

        );
        LOGGER.info("Начали делать жанры");
        try {
            long startTime = System.currentTimeMillis();
            genreRepository.saveAll(genres).blockLast();
            LOGGER.info("Успешно добавлено {} жанры за {} мс", genres.size(),
                    System.currentTimeMillis() - startTime);

        } catch (Exception e) {
            LOGGER.error("Непредвиденная ошибка при добавлении жанров: {}", e.getMessage(), e);
        }
        LOGGER.info("Завершена миграция insertGenres");
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "makarov")
    public void insertBooks(BookRepository bookRepository) {
        books = Arrays.asList(
                new Book("1","BookTitle_1", authors.get(0), genres.get(0)),
                new Book("2","BookTitle_2", authors.get(1), genres.get(1)),
                new Book("3", "BookTitle_3", authors.get(2), genres.get(2))
        );
         bookRepository.saveAll(books).blockLast();
    }
}

