package ru.makarov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.makarov.config.TestConfig;
import ru.makarov.domain.TestResult;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final TestConfig testConfig;

    private final LocalizedIOService ioService;

    @Override
    public void showResult(TestResult testResult) {
        ioService.printLine("");
        ioService.printLineLocalized("result.text");
        ioService.printFormattedLineLocalized("result.student", testResult.getStudent().getFullName());
        ioService.printFormattedLineLocalized("result.answered", testResult.getAnsweredQuestions().size());
        ioService.printFormattedLineLocalized("result.right", testResult.getRightAnswersCount());

        if (testResult.getRightAnswersCount() >= testConfig.getRightAnswersCountToPass()) {
            ioService.printLineLocalized("result.congratulations");
            return;
        }
        ioService.printLineLocalized("result.sorry");
    }
}
