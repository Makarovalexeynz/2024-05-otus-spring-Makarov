package ru.makarov.dao;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.makarov.config.AppProperties;
import org.mockito.Mock;
import ru.makarov.domain.Question;
import ru.makarov.exceptions.QuestionReadException;
import java.util.List;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThatList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CsvQuestionDaoTest {

    @Mock
    private AppProperties appProperties;

    QuestionDao questionDao;

    @BeforeEach
    public void setUp(){
        questionDao = new CsvQuestionDao(appProperties);
    }

    @DisplayName("Должен поймать исключение при путом is")
    @Test
    void shouldThrowQuestionReadExceptionException() {
        //no path to the source file
        assertThrows(NullPointerException.class,  questionDao::findAll);
    }

    @DisplayName("Должен выбрасить ошибку о неверном формате ответа")
    @Test
    void shouldThrowExceptionForInvalidQuestionFormat() {
        given(appProperties.getTestFileName()).willReturn("invalidQuestionTest.csv");
        assertThrows(QuestionReadException.class, questionDao::findAll);
    }

    @DisplayName("Должен загрузить два вопроса из тестового файла")
    @Test
    void shouldLoadTwoQuestionsFromTestFile() {
        given(appProperties.getTestFileName()).willReturn("questionTest.csv");

        List<Question> questions = questionDao.findAll();

        assertThatList(questions)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .extracting(Question::text)
                .containsExactlyInAnyOrder("First question", "Second question");
    }
}

