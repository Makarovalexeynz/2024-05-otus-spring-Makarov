package ru.makarov;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.makarov.service.TestRunnerService;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {

        //Создать контекст Spring Boot приложения
        ApplicationContext context = SpringApplication.run(Application.class, args); //new
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();

    }
}
