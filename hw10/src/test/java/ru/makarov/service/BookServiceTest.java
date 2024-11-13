package ru.makarov.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.annotation.Import;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.makarov.dto.AuthorDto;
import ru.makarov.dto.BookDto;
import ru.makarov.dto.GenreDto;
import ru.makarov.mappers.AuthorMapper;
import ru.makarov.mappers.BookMapper;
import ru.makarov.mappers.GenreMapper;
import ru.makarov.models.Author;
import ru.makarov.models.Book;
import ru.makarov.models.Genre;
import ru.makarov.services.BookServiceImpl;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import({BookServiceImpl.class, BookMapper.class, AuthorMapper.class, GenreMapper.class})
public class BookServiceTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private GenreMapper genreMapper;

    @Autowired
    private BookMapper bookMapper;

    private List<Author> dbAuthors;

    private List<AuthorDto> dbAuthorsDto;

    private List<Genre> dbGenres;
    private List<GenreDto> dbGenresDto;

    private List<BookDto> dbBooksDto;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbAuthorsDto = dbAuthors.stream().map(authorMapper::toDto).collect(Collectors.toList());
        dbGenres = getDbGenres();
        dbGenresDto = getDbGenres().stream().map(genreMapper::toDto).collect(Collectors.toList());
        dbBooks = getDbBooks(dbAuthors, dbGenres);
        dbBooksDto = dbBooks.stream().map(bookMapper::toDto).collect(Collectors.toList());
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnAllBooks() {

        var actualBooks = bookService.findAll();
        var expectedBooks = dbBooksDto;
        assertThat(actualBooks).isNotEmpty()
                .hasSize(3)
                .hasOnlyElementsOfType(BookDto.class)
                .usingRecursiveComparison()
                .isEqualTo(expectedBooks);
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnBookById(Book expectedBook) {
        var actualBook = bookService.findById(expectedBook.getId());
        assertThat(actualBook)
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять книгу")
    @Test
    void shouldInsertBook() {
        String title = "New Book Title";
        long authorId = 1L;
        long genreId = 2L;

        Book expectedBook = new Book(0, title,
                new Author(authorId, "Author_" + authorId),
                new Genre(genreId, "Genre_" + genreId));

        var expectedCreateBookDto = bookMapper.toCreateDto((bookMapper.toDto(expectedBook)));

        BookDto insertedBook = bookService.create(expectedCreateBookDto);

        assertNotNull(insertedBook);
        assertThat(insertedBook.getId()).isGreaterThan(0);

        var allBooks = bookService.findAll();
        assertThat(allBooks).hasSize(4);

        assertThat(allBooks).contains(insertedBook);

        BookDto foundBook = bookService
                .findById(insertedBook.getId());

        assertThat(foundBook)
                .usingRecursiveComparison()
                .isEqualTo(insertedBook);
    }

    @DisplayName("Должен изменять книгу")
    @Test
    void shouldUpdateBook(){

        Long bookId = 1L;
        String updatedTitle = "Updated Book Title";
        Author updatedAuthor = dbAuthors.get(1);
        Genre updatedGenre = dbGenres.get(2);

        Book expectedBook = new Book(bookId,updatedTitle,
                new Author(updatedAuthor.getId(),updatedAuthor.getFullName()),
                new Genre(updatedGenre.getId(), updatedGenre.getName()));

        BookDto expectedBookDto = bookMapper.toDto(expectedBook);

        var expectedBookUpdateDto = bookMapper.toUpdateDto(expectedBookDto);

        BookDto updatedBookDto = bookService.update(expectedBookUpdateDto);
        assertNotNull(updatedBookDto);
        var allBook = bookService.findAll();
        assertThat(allBook).contains(updatedBookDto);
        assertThat(updatedBookDto).usingRecursiveComparison().isEqualTo(expectedBookDto);
    }

    @DisplayName("Должен удалять книгу")
    @Test
    void shouldDeleteBook(){
        Long bookIdToDelete = 1L;

        BookDto existingBookDto = bookService.findById(bookIdToDelete);

        bookService.deleteById(bookIdToDelete);

        var remainingBooks = bookService.findAll();
        assertThat(remainingBooks).hasSize(2);

        assertThat(remainingBooks).doesNotContain(existingBookDto);
    }


    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id, "BookTitle_" + id, dbAuthors.get(id - 1), dbGenres.get(id - 1)))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }


}
