package ru.makarov.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.makarov.dto.BookCreateDto;
import ru.makarov.dto.BookDto;
import ru.makarov.dto.BookUpdateDto;
import ru.makarov.exceptions.NotFoundException;
import ru.makarov.mappers.AuthorMapper;
import ru.makarov.mappers.BookMapper;
import ru.makarov.mappers.GenreMapper;
import ru.makarov.models.Book;
import ru.makarov.repositories.AuthorRepository;
import ru.makarov.repositories.BookRepository;
import ru.makarov.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    @GetMapping("/api/v1/books")
    public Flux<BookDto> getBooks() {
        return bookRepository.findAll().map(bookMapper::toDto);
    }

    @GetMapping("/api/v1/books/{id}")
    public Mono<Book> getBook(@PathVariable String id) {
        return bookRepository.findById(id);
    }

    @PostMapping("/api/v1/books")
    public Mono<Book> createBook(@RequestBody BookCreateDto bookCreateDto) {


        return authorRepository.findById(bookCreateDto.getAuthorId())
                .flatMap(author ->
                        genreRepository.findById(bookCreateDto.getGenreId())
                                .flatMap(genre -> {
                                    Book book = new Book(null, bookCreateDto.getTitle(), author, genre);
                                    return bookRepository.save(book);
                                })
                                .onErrorMap(e -> new NotFoundException("Ошибка сохранения книги"))
                )
                .onErrorMap(e -> new NotFoundException("Ошибка поиска автора или жанра"));
    }

    @DeleteMapping("/api/v1/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBookById(@PathVariable("id") String id) {
        return bookRepository.deleteById(id);
    }

    @PutMapping("/api/v1/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Book> updateBook(@PathVariable String id, @Valid @RequestBody BookUpdateDto bookUpdateDto) {
        return bookRepository.findById(id)
                .flatMap(book -> {
                    book.setTitle(bookUpdateDto.getTitle());

                    return authorRepository.findById(bookUpdateDto.getAuthorId())

                            .flatMap(author -> {
                                book.setAuthor(author);

                                    return  genreRepository.findById(bookUpdateDto.getGenreId())
                                            .flatMap(genre -> {
                                                book.setGenre(genre);
                                                return bookRepository.save(book);
                                            });

                            })
                            .switchIfEmpty(Mono.error
                                    (new NotFoundException(
                                            "Автор не найден с id: " + bookUpdateDto.getAuthorId())))
                            .onErrorMap(ex -> new NotFoundException("Ошибка при обновлении автора книги"));
                })
                .switchIfEmpty(Mono.error(new NotFoundException("Книга не найдена с id: " + id)));
    }
}
