package ru.makarov.util;

import ru.makarov.domain.Question;

public class QuestionUtil {
    private QuestionUtil() {
    }

    public static String formatQuestion(Question question) {

        var result = new StringBuilder(String.format("%s:\n", question.text()));
        var answers = question.answers();

        int currentAnswerNo = 1;
        for (var answer : answers) {
            result.append(String.format("%d) %s\n", currentAnswerNo++, answer.text()));
        }
        return result.toString();
    }
}


