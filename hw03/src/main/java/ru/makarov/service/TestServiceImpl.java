package ru.makarov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.makarov.dao.QuestionDao;
import ru.makarov.domain.Question;
import ru.makarov.domain.Student;
import ru.makarov.domain.TestResult;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {
    public static final Integer FIRST_ANSWER_NUMBER = 1;

    public static final String INVALID_ANSWER_NUMBER_TEMPLATE = "The answer is incorrect. " +
            "The answer should be a number from %s to %s.";

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            var answersCount = question.answers().size();
            var answerIndex = ioService.readIntForRangeWithPrompt(
                    FIRST_ANSWER_NUMBER,
                    answersCount,
                    formatQuestion(question),
                    String.format(INVALID_ANSWER_NUMBER_TEMPLATE, FIRST_ANSWER_NUMBER, answersCount)
            );
            var answer = question.answers().get(answerIndex - 1);
            var isAnswerValid = answer.isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    static String formatQuestion(Question question) {

        var result = new StringBuilder(String.format("%s:\n", question.text()));
        var answers = question.answers();

        int currentAnswerNo = 1;
        for (var answer : answers) {
            result.append(String.format("%d) %s\n", currentAnswerNo++, answer.text()));
        }
        return result.toString();
    }
}
