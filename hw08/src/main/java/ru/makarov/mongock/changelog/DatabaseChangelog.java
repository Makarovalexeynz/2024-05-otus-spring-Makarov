package ru.makarov.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.makarov.models.Author;
import ru.makarov.models.Book;
import ru.makarov.models.Comment;
import ru.makarov.models.Genre;
import ru.makarov.repositories.AuthorRepository;
import ru.makarov.repositories.BookRepository;
import ru.makarov.repositories.CommentRepository;
import ru.makarov.repositories.GenreRepository;

import java.util.Arrays;
import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    private List<Author> authors;

    private List<Genre> genres;

    private List<Book> books;

    private List<Comment> comments;

    @ChangeSet(order = "001", id = "dropDb", author = "Makarov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "Makarov")
    public void insertAuthors(AuthorRepository authorRepository) {
        authors = Arrays.asList(new Author("1","Author_1"),
                new Author("2","Author_2"),
                new Author("3","Author_3"));
        authorRepository.saveAll(authors);
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "Makarov")
    public void insertGenres(GenreRepository genreRepository) {
        genres = Arrays.asList(new Genre("1","Genre_1"),
                new Genre("2","Genre_2"),
                new Genre("3","Genre_3")

        );
        genreRepository.saveAll(genres);
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "makarov")
    public void insertBooks(BookRepository bookRepository) {
        books = Arrays.asList(
                new Book("1","BookTitle_1", authors.get(0), genres.get(0)),
                new Book("2","BookTitle_2", authors.get(1), genres.get(1)),
                new Book("3", "BookTitle_3", authors.get(2), genres.get(2))
        );
        books = bookRepository.saveAll(books);
    }

    @ChangeSet(order = "005", id = "insertComments", author = "makarov")
    public void insertComments(CommentRepository commentRepository, BookRepository bookRepository) {
        List<Comment> comments = Arrays.asList(
                new Comment("1","CommentText_1", books.get(0)),
                new Comment("2", "CommentText_2", books.get(1)),
                new Comment("3","CommentText_3", books.get(2))
        );
        commentRepository.saveAll(comments);
    }
}

