package ru.makarov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.makarov.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
