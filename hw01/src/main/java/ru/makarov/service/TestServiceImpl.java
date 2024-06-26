package ru.makarov.service;

import lombok.RequiredArgsConstructor;
import ru.makarov.dao.QuestionDao;
import ru.makarov.domain.Question;
import ru.makarov.util.QuestionUtil;

import java.util.List;


@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao; //добавил

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questionList; // добавил
        questionList = questionDao.findAll();

        questionList.forEach(question -> ioService.printLine(QuestionUtil.formatQuestion(question)));
    }

        // Получить вопросы из дао и вывести их с вариантами ответов

}
