package ru.makarov.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.makarov.dto.AuthorDto;
import ru.makarov.dto.BookDto;
import ru.makarov.dto.GenreDto;
import ru.makarov.mappers.AuthorMapper;
import ru.makarov.mappers.BookMapper;
import ru.makarov.mappers.GenreMapper;
import ru.makarov.models.Author;
import ru.makarov.models.Book;
import ru.makarov.models.Genre;
import ru.makarov.services.BookService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import({BookMapper.class, AuthorMapper.class, GenreMapper.class})
public class RestBookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private GenreMapper genreMapper;

    private List<Author> dbAuthors;
    private List<AuthorDto> dbAuthorsDto;
    private List<Genre> dbGenres;
    private List<GenreDto> dbGenresDto;
    private List<Book> dbBooks;
    private List<BookDto> dbBooksDto;

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

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbAuthorsDto = dbAuthors.stream().map(authorMapper::toDto).collect(Collectors.toList());
        dbGenres = getDbGenres();
        dbGenresDto = getDbGenres().stream().map(genreMapper::toDto).collect(Collectors.toList());
        dbBooks = getDbBooks(dbAuthors, dbGenres);
        dbBooksDto = dbBooks.stream().map(bookMapper::toDto).collect(Collectors.toList());
    }

    @DisplayName("Должен возвращать все книги")
    @Test
    void testGetBooks() throws Exception{
        List<BookDto> expectedBooks = dbBooksDto;

        when(bookService.findAll()).thenReturn(expectedBooks);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedBooks)));
    }

    @DisplayName("Должен создавать новую книгу")
    @Test
    void shouldAddNewBook() throws Exception {

        var newBookDto = bookMapper.toDto(new Book(4L, "newTitle",
                new Author(4L,"NewAuthor"),
                new Genre(4L,"newGenre")));

        var newCreateBookDto = bookMapper.toCreateDto(newBookDto);

        when(bookService.insert(newCreateBookDto))
                .thenReturn(newBookDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCreateBookDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(newBookDto)));
    }

    @DisplayName("Должен обновлять книгу")
    @Test
    void shouldEditBook() throws Exception {
        BookDto newBookDto = bookMapper.toDto(
                new Book(1L, "newTitle", dbAuthors.get(0), dbGenres.get(0)));

        var newBookUpdateDto = bookMapper.toUpdateDto(newBookDto);

        when(bookService.update(newBookUpdateDto)).thenReturn(newBookDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBookUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(newBookDto)));
    }

    @DisplayName("должен удалять книгу")
    @Test
    void testDeleteBookById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/books/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Должен возвращать книгу по ИД")
    @Test
    void testGetBookById() throws Exception{

        var expectedBook = dbBooksDto.get(0);

        when(bookService.findById(1)).thenReturn(expectedBook);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedBook)));
    }
}





