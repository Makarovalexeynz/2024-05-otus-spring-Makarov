package ru.makarov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.makarov.domain.Student;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final LocalizedIOService ioService;

    @Override
    public Student determineCurrentStudent() {
        var firstName = ioService.readStringWithPromptLocalized("student.name");
        var lastName = ioService.readStringWithPromptLocalized("student.lastname");
        return new Student(firstName, lastName);
    }
}
