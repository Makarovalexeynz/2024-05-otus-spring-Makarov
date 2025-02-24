package ru.makarov.languagelearning.languageLearningApp.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import ru.makarov.languagelearning.languageLearningApp.dto.TranslationCreateDTO;
import ru.makarov.languagelearning.languageLearningApp.dto.TranslationDTO;
import ru.makarov.languagelearning.languageLearningApp.dto.TranslationUpdateDTO;
import ru.makarov.languagelearning.languageLearningApp.services.TranslationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationService translationService;

    @GetMapping(path = "api/v1/translations", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TranslationDTO> getListTranslation() {
        return  translationService.findAll();
    }

    @GetMapping(path = "api/v1/translations/{id}")
    public TranslationDTO getTranslationById(@PathVariable long id) {
        return translationService.findById(id);
    }

    @GetMapping(path = "api/v1/translations/flashcard/{flashcardId}")
    public List<TranslationDTO> getTranslationByFlashcardId(@PathVariable Long flashcardId) {
        return  translationService.findByFlashcardId(flashcardId);
    }

    @PostMapping(path = "api/v1/translations")
    public TranslationDTO createTranslation (@RequestBody TranslationCreateDTO translationCreateDTO) {
        return translationService.create(translationCreateDTO);
    }

    @DeleteMapping(path = "api/v1/translations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTranslation(@PathVariable long id) {
        translationService.deleteById(id);
    }

    @PutMapping(path = "api/v1/translations/{id}")
    public TranslationDTO updateTranslation(@RequestBody TranslationUpdateDTO translationUpdateDTO) {
        return translationService.update(translationUpdateDTO);
    }
}
