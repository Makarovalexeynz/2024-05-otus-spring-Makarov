package ru.makarov.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.makarov.services.CatepillarService;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final CatepillarService catepillarService;

    @Override
    public void run(String... args) {
        catepillarService.startGenerateOrderLoop();
    }
}
