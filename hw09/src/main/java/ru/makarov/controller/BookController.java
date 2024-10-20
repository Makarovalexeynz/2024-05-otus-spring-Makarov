package ru.makarov.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.makarov.dto.BookDto;
import ru.makarov.mappers.BookMapper;
import ru.makarov.services.AuthorService;
import ru.makarov.services.BookService;
import ru.makarov.services.CommentService;
import ru.makarov.services.GenreService;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentService commentService;

    private final BookMapper bookMapper;

    @GetMapping("/")
    public String listPage(Model model) {
        List <BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "list";
    }

    @GetMapping("/add")
    public String create(Model model) {
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("bookDto", new BookDto());
        return "addBook";
    }

    @PostMapping("/add")
    public String create(@Valid BookDto bookDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "addBook";
        }

        bookService.insert(
                bookDto.getTitle(),
                bookDto.getAuthor().getId(),
                bookDto.getGenre().getId());

        return "redirect:/";
    }

    @GetMapping(value = "/edit/{id}")
    public String edit(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("bookDto", book);
        return "editBook";
    }

    @PostMapping("/edit")
    public String edit(@Valid BookDto bookDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "editBook";
        }

        bookService.update(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthor().getId(),
                bookDto.getGenre().getId());
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String book(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id);
        model.addAttribute("bookDto", book);
        return "book";
    }

    @PostMapping("/delete")
    public String deleteBook(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}
