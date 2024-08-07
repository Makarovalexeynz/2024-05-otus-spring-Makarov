package ru.makarov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.makarov.dao.QuestionDao;
import ru.makarov.domain.Student;
import ru.makarov.domain.TestResult;
import ru.makarov.util.QuestionUtil;

import static ru.makarov.util.QuestionConstants.FIRST_ANSWER_NUMBER;
import static ru.makarov.util.QuestionConstants.INVALID_ANSWER_NUMBER_TEMPLATE;


@RequiredArgsConstructor
@Service //new
public class TestServiceImpl implements TestService {

    private final IOService ioService;

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
                    QuestionUtil.formatQuestion(question),
                    String.format(INVALID_ANSWER_NUMBER_TEMPLATE, FIRST_ANSWER_NUMBER, answersCount)
            );
            var answer = question.answers().get(answerIndex - 1);
            var isAnswerValid = answer.isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
