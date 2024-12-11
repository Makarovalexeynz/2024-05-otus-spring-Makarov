package ru.makarov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
        System.out.printf("Чтобы перейти на страницу сайта открывай: %n%s%n",
                "http://localhost:8080 \n lgn:user pswrd:user \n lgn:admin pswrd:admin");
    }
}