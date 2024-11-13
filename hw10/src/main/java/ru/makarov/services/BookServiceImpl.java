package ru.makarov.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.makarov.dto.BookCreateDto;
import ru.makarov.dto.BookDto;
import ru.makarov.dto.BookUpdateDto;
import ru.makarov.exceptions.NotFoundException;
import ru.makarov.mappers.BookMapper;
import ru.makarov.models.Book;
import ru.makarov.repositories.AuthorRepository;
import ru.makarov.repositories.BookRepository;
import ru.makarov.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(long id) {
       var findByIdBook = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found"));
        return bookMapper.toDto(findByIdBook);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookMapper::toDto).toList();
    }

    @Transactional
    @Override
    public BookDto create(BookCreateDto bookCreateDto) {
        var author = authorRepository.findById(bookCreateDto.getAuthorId())
                .orElseThrow(() ->
                        new NotFoundException("Author with id %d not found".formatted(bookCreateDto.getAuthorId())));
        var genre = genreRepository.findById(bookCreateDto.getGenreId())
                .orElseThrow(() ->
                        new NotFoundException("Genre with id %d not found".formatted(bookCreateDto.getGenreId())));
        var book = new Book(0 ,bookCreateDto.getTitle(), author, genre);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookDto update(BookUpdateDto bookUpdateDto) {
        var book = bookRepository.findById(bookUpdateDto.getId())
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(bookUpdateDto.getId())));
        var author = authorRepository.findById(bookUpdateDto.getAuthorId())
                .orElseThrow(() ->
                        new NotFoundException("Author with id %d not found".formatted(bookUpdateDto.getAuthorId())));
        var genre = genreRepository.findById(bookUpdateDto.getGenreId())
                .orElseThrow(() ->
                        new NotFoundException("Genre with id %d not found".formatted(bookUpdateDto.getGenreId())));
        var updateBook = new Book(bookUpdateDto.getId(),bookUpdateDto.getTitle(), author, genre);
        return bookMapper.toDto(bookRepository.save(updateBook));

    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
