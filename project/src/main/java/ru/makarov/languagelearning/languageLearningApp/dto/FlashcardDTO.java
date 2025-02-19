package ru.makarov.languagelearning.languageLearningApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardDTO {

    private Long id;

    private String foreignWord;

    private Long foreignLanguageId;

    private List<String> tags;

    private List<String> nativeWords;
}
