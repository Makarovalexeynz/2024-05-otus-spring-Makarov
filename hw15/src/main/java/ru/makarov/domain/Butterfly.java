package ru.makarov.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Butterfly {
    private Long id;

    private String status;
}
