package ru.makarov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.makarov.exceptions.EntityNotFoundException;
import ru.makarov.models.Book;
import ru.makarov.services.AuthorService;
import ru.makarov.services.BookService;
import ru.makarov.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String listPage(Model model) {
        List <Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "list";
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "addBook";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book) {
        bookService.insert(book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable("id") long id, Model model) {
        Book  book  =  bookService.findById(id).orElseThrow(() -> new EntityNotFoundException("No"));
        model.addAttribute("book",  book);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return  "editBook";
    }

    @PostMapping("/edit")
    public String editBook(Book book) {
                bookService.update(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteBook(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}
