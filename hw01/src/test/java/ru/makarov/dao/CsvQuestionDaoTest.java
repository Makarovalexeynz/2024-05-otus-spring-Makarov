package ru.makarov.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.makarov.config.AppProperties;
import ru.makarov.exceptions.QuestionReadException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CsvQuestionDaoTest {
    @Test
    public void wrongfileNameShouldThrowExeption(){
        CsvQuestionDao dao = new CsvQuestionDao(new AppProperties("Qquetion.csv"));
        assertThrows(QuestionReadException.class, dao::findAll);
    }

    @Test
    public void rightFileName(){
        CsvQuestionDao dao = new CsvQuestionDao(new AppProperties("question.csv"));
        assertDoesNotThrow(dao::findAll);
    }



}