package ru.makarov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.makarov.models.Author;
import ru.makarov.models.Book;
import ru.makarov.models.Genre;
import ru.makarov.services.AuthorService;
import ru.makarov.services.BookService;
import ru.makarov.services.GenreService;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
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
    void shouldReturnListOfBooks() throws Exception {

        when(bookService.findAll()).thenReturn(dbBooks);

        mvc.perform(get("/")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", dbBooks))
                .andExpect(view().name("list"));
    }


    @DisplayName("Должен создавать новую книгу")
    @Test
    void shouldAddNewBook() throws Exception {

            Book newBook = new Book(4L, "newTitle",
                    new Author(4L,"NewAuthor"),
                    new Genre(4L,"newGenre"));

            when(bookService.insert(newBook.getTitle(), newBook.getAuthor().getId(), newBook.getGenre().getId()))
                    .thenReturn(newBook);

            mvc.perform(post("/add").flashAttr("book", newBook)).andExpect(redirectedUrl("/"));
    }

    @DisplayName("Должен обновлять книгу")
    @Test
    void shouldEditBook() throws Exception {
        Book editBook = new Book(1L, "newTitle", dbAuthors.get(0), dbGenres.get(0));

        when(bookService.update(dbBooks.get(0).getId(),
                dbBooks.get(0).getTitle(),
                dbAuthors.get(0).getId(),
                dbGenres.get(0).getId())).thenReturn(editBook);

        mvc.perform(post("/edit")
                        .flashAttr("book", editBook))
                .andExpect(redirectedUrl("/"));
    }

    @DisplayName("должен удалять книгу")
    @Test
    void shouldDeleteBook() throws Exception {
        doNothing().when(bookService);
        mvc.perform(post("/delete").param("id","1"))
                .andExpect(redirectedUrl("/"));
    }
}









