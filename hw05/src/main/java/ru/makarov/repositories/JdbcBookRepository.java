package ru.makarov.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.makarov.exceptions.EntityNotFoundException;
import ru.makarov.models.Author;
import ru.makarov.models.Book;
import ru.makarov.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcBookRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

@Override
public Optional<Book> findById(long id) {

        List<Book> books = namedParameterJdbcTemplate.query(
                    "select books.id, books.title, authors.id, authors.full_name, genres.id, genres.name " +
                        "from books " +
                        "join authors ON books.author_id = authors.id  " +
                        "join  genres ON books.genre_id = genres.id " +
                        "where books.id=:id",
                Map.of("id", id),
                new BookRowMapper());
    return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
    }

    @Override
    public List<Book> findAll() {

        return namedParameterJdbcTemplate.query(
                "select books.id, books.title, authors.id, authors.full_name, genres.id, genres.name " +
                "from books " +
                "join authors ON books.author_id = authors.id " +
                "join genres ON books.genre_id = genres.id"
                , new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM books WHERE id = :id";
        Map<String, Object> params = Collections.singletonMap("id", id);

        int rowsAffected = namedParameterJdbcTemplate.update(sql, params);

        if (rowsAffected == 0) {
            throw new EmptyResultDataAccessException("Book with id " + id + " not found", 1);
        }
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                "insert into books(title, author_id, genre_id) values (:title, :authorId, :genreId)",
                new MapSqlParameterSource() // Вернули MapSqlParameterSource
                        .addValue("title", book.getTitle())
                        .addValue("authorId", book.getAuthor().getId())
                        .addValue("genreId", book.getGenre().getId()),
                keyHolder
        );

        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        int rowsAffected = namedParameterJdbcTemplate.update(
                "UPDATE books SET title = :title, author_id = :authorId, genre_id = :genreId " +
                        "WHERE id = :id",
                Map.of(
                        "title", book.getTitle(),
                        "authorId", book.getAuthor().getId(),
                        "genreId", book.getGenre().getId(),
                        "id", book.getId()
                )
        );

        if (rowsAffected == 0) {
            throw new EntityNotFoundException("Book with id " + book.getId() + " not found");
        }

        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            long authorId = rs.getLong("authors.id");
            String authorFullName = rs.getString("authors.full_name");
            long genreId = rs.getLong("genres.id");
            String genreName = rs.getString("genres.name");

            Author author = new Author(authorId, authorFullName);
            Genre genre = new Genre(genreId,genreName);
            return new Book(id, title, author, genre);
        }
    }
}
