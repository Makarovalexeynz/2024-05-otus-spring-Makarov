package ru.makarov.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.makarov.dto.BookCreateDto;
import ru.makarov.dto.BookDto;
import ru.makarov.dto.BookUpdateDto;
import ru.makarov.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/v1/books")
    public List<BookDto> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/v1/books/{id}")
    public BookDto getBook(@PathVariable long id) {
        return bookService.findById(id);
    }

    @PostMapping("/api/v1/books")
    public BookDto createBook(@Valid @RequestBody BookCreateDto bookCreateDto) {
        return bookService.create(bookCreateDto);
    }

    @DeleteMapping("/api/v1/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookById(@PathVariable long id) {
        bookService.deleteById(id);
    }


    @PutMapping("/api/v1/books/{id}")
    public BookDto updateBook(@PathVariable Long id, @Valid @RequestBody BookUpdateDto bookUpdateDto) {
        return bookService.update(bookUpdateDto);
    }
}
