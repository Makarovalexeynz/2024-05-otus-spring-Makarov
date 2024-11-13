package ru.makarov.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.makarov.dto.ErrorDto;
import ru.makarov.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(NotFoundException.class)
    public ErrorDto handleEntityNotFound(NotFoundException ex) {
        LOGGER.error("Resource not found", ex);
        return new ErrorDto("Resource not found", HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ErrorDto handleEntityNotFound(NoResourceFoundException ex) {
        LOGGER.error("Bad URL", ex);
        return new ErrorDto("Bad URL", HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
            LOGGER.error("Validation error", ex);
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errors);
        }

    @ExceptionHandler(Exception.class)
    public ErrorDto handleException(Exception ex) {
        LOGGER.error("Internal server error", ex);
        return new ErrorDto("Internal server error" ,HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
