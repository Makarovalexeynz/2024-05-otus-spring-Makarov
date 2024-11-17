package ru.makarov.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.makarov.page.BookPageController;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookPageController.class)
public class BookPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetListBookPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("list"));
    }

    @Test
    void testGetCreateBookPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/book"))
                .andExpect(status().isOk())
                .andExpect(view().name("addBook"));
    }

    @Test
    void testGetEditBookPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/book/123"))
                .andExpect(status().isOk())
                .andExpect(view().name("editBook"))
                .andExpect(MockMvcResultMatchers.model().attribute("bookId", 123L));
    }
}
