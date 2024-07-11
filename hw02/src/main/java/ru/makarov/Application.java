package ru.makarov;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import ru.makarov.service.TestRunnerService;
import org.springframework.context.ApplicationContext;

@PropertySource("classpath:application.properties")
@ComponentScan  //new
public class Application {
    public static void main(String[] args) {

        //Создать контекст на основе Annotation/Java конфигурирования
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);  // new
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();

    }
}
