package ru.makarov.service;

import ru.makarov.domain.Student;
import ru.makarov.domain.TestResult;

public interface TestService {
    TestResult executeTestFor(Student student);
}
