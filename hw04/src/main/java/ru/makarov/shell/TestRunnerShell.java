package ru.makarov.shell;


import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import ru.makarov.service.TestRunnerService;
import ru.makarov.service.TestRunnerServiceImpl;


@ShellComponent(value = "Start Application")
@RequiredArgsConstructor
public class TestRunnerShell {

    private final TestRunnerServiceImpl testRunnerService;

    @ShellMethod(value = "Start test", key = {"start"})
    public void start() {
        testRunnerService.run();
    }
}
