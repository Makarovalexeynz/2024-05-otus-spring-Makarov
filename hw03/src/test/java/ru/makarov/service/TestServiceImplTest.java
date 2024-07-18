package ru.makarov.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.makarov.dao.QuestionDao;
import ru.makarov.domain.Answer;
import ru.makarov.domain.Question;
import ru.makarov.domain.Student;
import ru.makarov.domain.TestResult;

import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static ru.makarov.service.TestServiceImpl.*;


@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    @Mock
    private QuestionDao questionDao;

    @Mock
    private LocalizedIOService ioService;
    @InjectMocks
    private TestServiceImpl testService;

    @BeforeEach
    void setUp() {
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @DisplayName("Должен вернуть пустой результат для пустого пользовате")
    @Test
    void shouldReturnEmptyResultForNullStudent() {
        TestResult testResult = testService.executeTestFor(null);
        assertThat(testResult).isNotNull();
        assertThat(testResult.getStudent()).isNull();
        assertThatList(testResult.getAnsweredQuestions()).isEmpty();
        assertThat(testResult.getRightAnswersCount()).isEqualTo(0);
    }

    @DisplayName("Должен выполнить тест с двумя вопросами и одним правильным ответом")
    @Test
    public void  shouldExecuteTestWithTwoQuestionsAndOneCorrectAnswer() {
        var questions = List.of(
                new Question("First question", List.of(new Answer("ans1", true), new Answer("ans2", false))),
                new Question("Second question", List.of(new Answer("ans1", false), new Answer("ans2", true)))
        );
        BDDMockito.given(questionDao.findAll()).willReturn(questions);

        for (var question : questions) {
            BDDMockito.given(ioService.readIntForRangeWithPrompt(
                    1, 2, formatQuestion(question), ioService.getMessage("The answer is incorrect. The answer should be a number from %s to %s",1,2)
            )).willReturn(1);
        }

        var student = new Student("Name", "Surname");
        var testResult = testService.executeTestFor(student);

        // Assert
        verify(questionDao, times(1)).findAll();
        verify(ioService, times(1)).printLine("");
        verify(ioService, times(1)).printFormattedLineLocalized("test.text");
        verify(ioService, times(0)).readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString());

        assertThat(testResult.getStudent()).isEqualTo(student);
        assertThatList(testResult.getAnsweredQuestions()).hasSize(2);
        assertThat(testResult.getRightAnswersCount()).isEqualTo(1);
    }
}