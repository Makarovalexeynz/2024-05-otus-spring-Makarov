package ru.makarov;


import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongock
@EnableMongoRepositories
public class Application {

    public static void main(String[] args) throws Exception {

        SpringApplication.run(Application.class, args);

    }
}
