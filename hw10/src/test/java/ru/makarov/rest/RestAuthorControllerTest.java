package ru.makarov.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.makarov.dto.AuthorDto;
import ru.makarov.services.AuthorService;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
public class RestAuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAuthors() throws Exception {

        List<AuthorDto> expectedAuthors = Arrays.asList(
                new AuthorDto(1L, "Author_1"),
                new AuthorDto(1L, "Author_2")
        );

        when(authorService.findAll()).thenReturn(expectedAuthors);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/authors")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.content()
                        .json(objectMapper.writeValueAsString(expectedAuthors)));
    }
}
