package ru.makarov.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component  //new
@Data
public class AppProperties implements TestConfig, TestFileNameProvider {

    // внедрить свойство из application.properties
    @Value("${test.rightAnswersCountToPass}")  //new
    private int rightAnswersCountToPass;

    // внедрить свойство из application.properties
    @Value("${test.fileName}")    //new
    private String testFileName;
}