package ru.makarov.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    @Override
    public void run() {
        testService.executeTest();
    }
}
