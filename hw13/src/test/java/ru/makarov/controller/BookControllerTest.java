package ru.makarov.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.makarov.dto.AuthorDto;
import ru.makarov.dto.BookDto;
import ru.makarov.dto.GenreDto;
import ru.makarov.mappers.AuthorMapper;
import ru.makarov.mappers.BookMapper;
import ru.makarov.mappers.GenreMapper;
import ru.makarov.models.Author;
import ru.makarov.models.Book;
import ru.makarov.models.Genre;
import ru.makarov.models.User;
import ru.makarov.repositories.UserRepository;
import ru.makarov.security.SecurityConfig;
import ru.makarov.services.AuthorService;
import ru.makarov.services.BookService;
import ru.makarov.services.CommentService;
import ru.makarov.services.GenreService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import({BookMapper.class, AuthorMapper.class, GenreMapper.class, SecurityConfig.class})
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BookService bookService;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private GenreMapper genreMapper;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

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


    @DisplayName("Должен возвращать все книги")
    @Test
    @WithMockUser
    void shouldReturnListOfBooks() throws Exception {

        when(bookService.findAll()).thenReturn(dbBooksDto);

        mvc.perform(get("/")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", dbBooksDto))
                .andExpect(view().name("list"));
    }


    @DisplayName("Должен создавать новую книгу")
    @Test
    @WithMockUser
    void shouldAddNewBook() throws Exception {

            var newBookDto = bookMapper.toDto(new Book(4L, "newTitle",
                    new Author(4L,"NewAuthor"),
                    new Genre(4L,"newGenre")));

            var newCreateBookDto = bookMapper.toCreateDto(newBookDto);

            when(bookService.insert(newCreateBookDto))
                    .thenReturn(newBookDto);

            mvc.perform(post("/add").with(csrf())
                    .flashAttr("bookDto", newCreateBookDto))
                    .andExpect(redirectedUrl("/"));
    }

    @DisplayName("Должен обновлять книгу")
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldEditBook() throws Exception {
        BookDto newBookDto = bookMapper.toDto(
                new Book(1L, "newTitle", dbAuthors.get(0), dbGenres.get(0)));

        var newBookUpdateDto = bookMapper.toUpdateDto(newBookDto);

        when(bookService.update(newBookUpdateDto)).thenReturn(newBookDto);

        mvc.perform(post("/edit")
                        .with(csrf())
                        .flashAttr("bookDto", newBookUpdateDto))
                .andExpect(redirectedUrl("/"));
    }

    @DisplayName("Должен ловить код 401, а не обновлять книгу")
    @Test
    @WithMockUser(roles = {"USER"})
    void shouldNotEditBook() throws Exception {
        BookDto newBookDto = bookMapper.toDto(
                new Book(1L, "newTitle", dbAuthors.get(0), dbGenres.get(0)));

        var newBookUpdateDto = bookMapper.toUpdateDto(newBookDto);

        when(bookService.update(newBookUpdateDto)).thenReturn(newBookDto);

        mvc.perform(post("/edit")
                        .with(csrf())
                        .flashAttr("bookDto", newBookUpdateDto))
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("должен удалять книгу")
    @Test
    @WithMockUser
    void shouldDeleteBook() throws Exception {
        doNothing().when(bookService);
        mvc.perform(post("/delete").with(csrf()).param("id","1"))
                .andExpect(redirectedUrl("/"));
    }
    @DisplayName("Редирект на /login для неавторизованного пользователя")
    @Test
    @WithAnonymousUser
    void shouldRedirect() throws Exception {

        mvc.perform(get("/")
                        .accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

    }


}









