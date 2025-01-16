package ru.makarov.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.makarov.repositories.BookRepository;

@Component("CustomHealth")
@RequiredArgsConstructor
public class CustomHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        try {
            boolean isDatabaseHealthy = checkDatabaseConnection();
            return isDatabaseHealthy ? Health.up().build() : Health.down()
                    .withDetail("error", "Ошибка соединения с базой данных").build();
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }

    private boolean checkDatabaseConnection() {
        try {
            bookRepository.findById(1L).orElse(null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}