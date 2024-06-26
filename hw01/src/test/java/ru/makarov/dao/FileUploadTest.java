package ru.makarov.dao;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileUploadTest {
    private static final String TEST_FILE_PATH = "src/test/resources/questionTest.csv";

    private static final String EXPECTED_CONTENT = "123";

    @Test
    void testFileLoading() throws IOException {
        // 1. Создание ResourceLoader
        ResourceLoader resourceLoader = new FileSystemResourceLoader();

        // 2. Загрузка ресурса
        Resource resource = resourceLoader.getResource(TEST_FILE_PATH);

        // 3. Проверки
        // 3.1. Проверка существования ресурса
        assertTrue(resource.exists(), "Файл не найден!");

        // 3.2. Проверка доступности ресурса для чтения
        assertTrue(resource.isReadable(), "Файл недоступен для чтения!");

        // 3.3. Проверка содержимого ресурса (если необходимо)
        String actualContent = Files.readString(Paths.get(resource.getURI()));
        assertEquals(EXPECTED_CONTENT, actualContent, "Содержимое файла не соответствует ожидаемому!");
    }
}


