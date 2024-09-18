package ru.makarov.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.makarov.exceptions.EntityNotFoundException;
import ru.makarov.models.Book;
import ru.makarov.models.Author;
import ru.makarov.models.Genre;
import ru.makarov.repositories.AuthorRepository;
import ru.makarov.repositories.BookRepository;
import ru.makarov.repositories.GenreRepository;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book create(String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        var book = new Book(null, title, author, genre);
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book update(String id, String title, String authorId, String genreId) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Книга с ID " + id + " не найдена."));

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Автор с ID " + id + " не найден."));

        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new IllegalArgumentException("Жанр с ID " + id + " не найден."));

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);

        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }
}
