package ru.makarov.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BookPageController {

    @GetMapping("/")
    public String getListBookPage() {
        return "list";
    }

    @GetMapping("/book")
    public String getCreateBookPage() {
        return "addBook";
    }

    @GetMapping("/book/{id}")
    public String getEditBookPage(@PathVariable("id") long id, Model model) {
        model.addAttribute("bookId", id);
        return "editBook";
    }
}
