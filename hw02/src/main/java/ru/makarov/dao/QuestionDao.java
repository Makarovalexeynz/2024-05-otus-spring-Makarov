package ru.makarov.dao;

import ru.makarov.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
}
