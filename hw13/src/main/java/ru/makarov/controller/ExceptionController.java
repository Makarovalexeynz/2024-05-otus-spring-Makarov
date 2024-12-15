package ru.makarov.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.makarov.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;


@ControllerAdvice
public class ExceptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(NotFoundException ex) {
        return ResponseEntity.badRequest().body("Resource not found");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleException500(AccessDeniedException ex) {
        LOGGER.error("Ошибка доступа", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new String("Only ADMIN can edit"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        LOGGER.error("Internal server error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new String("Internal server error"));
    }
}
