package ru.makarov.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdateDto {

    private Long id;

    @NotBlank(message = "Title must not be empty")
    @Size(min = 2, max = 20, message = "The title must be more than 2 letters and less than 20")
    private String title;

    @NotNull(message = "Author must not be empty")
    private Long authorId;

    @NotNull(message = "Genre must not be empty")
    private Long genreId;
}
