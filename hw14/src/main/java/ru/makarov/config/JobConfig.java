package ru.makarov.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.makarov.dto.BookUpdateDto;
import ru.makarov.models.Author;
import ru.makarov.models.AuthorMongo;
import ru.makarov.models.BookMongo;
import ru.makarov.models.GenreMongo;
import ru.makarov.models.Genre;
import ru.makarov.processors.AuthorProcessor;
import ru.makarov.processors.BookProcessor;
import ru.makarov.processors.GenreProcessor;
import ru.makarov.repositories.AuthorRepositoryMongo;
import ru.makarov.repositories.BookRepositoryMongo;
import ru.makarov.repositories.GenreRepositoryMongo;


import javax.sql.DataSource;
import java.util.HashMap;


@Configuration
@RequiredArgsConstructor
public class JobConfig {


    private static final Logger LOGGER = LoggerFactory.getLogger(JobConfig.class);

    private final MongoTemplate mongoTemplate;

    private final DataSource dataSource;

    private final JobRepository jobRepository;

    private final AuthorProcessor authorProcessor;

    private final GenreProcessor genreProcessor;

    private final BookProcessor bookProcessor;

    private final AuthorRepositoryMongo authorRepositoryMongo;

    private final GenreRepositoryMongo genreRepositoryMongo;

    private final BookRepositoryMongo bookRepositoryMongo;

    private final PlatformTransactionManager platformTransactionManager;

    private final JdbcTemplate jdbcTemplate;


    @Bean
    public Job mongoToJpaMigrationJob() {
        LOGGER.info("Joba");
        return new JobBuilder("mongoToJpaMigrationJob", jobRepository)
                .start(mongoToJpaMigrationStepAuthors())
                .next(mongoToJpaMigrationStepGenre())
                .next(mongoToJpaMigrationStepBook())
                .build();
    }

    @Bean
    public Step mongoToJpaMigrationStepAuthors() {
        LOGGER.info("Степ для Авторов");
        return new StepBuilder("mongoToJpaMigrationStepAuthors", jobRepository)
                .<AuthorMongo, Author>chunk(100, platformTransactionManager)
                .reader(mongoItemReaderAuthors())
                .processor(authorProcessor)
                .writer(authorJdbcBatchItemWriter())
                .build();
    }

    @Bean
    public Step mongoToJpaMigrationStepGenre() {
        LOGGER.info("Степ для Жанров");
        return new StepBuilder("mongoToJpaMigrationStepGenre", jobRepository)
                .<GenreMongo, Genre>chunk(100, platformTransactionManager)
                .reader(mongoItemReaderGenres())
                .processor(genreProcessor)
                .writer(genreJdbcBatchItemWriter())
                .build();
    }

    @Bean
    public Step mongoToJpaMigrationStepBook() {
        LOGGER.info("Степ для Книг");
        return new StepBuilder("mongoToJpaMigrationStepBook", jobRepository)
                .<BookMongo, BookUpdateDto>chunk(100, platformTransactionManager)
                .reader(mongoItemReaderBooks())
                .processor(bookProcessor)
                .writer(bookJdbcBatchItemWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<AuthorMongo> mongoItemReaderAuthors() {
        LOGGER.info("Шар ридера Авторов");
        return new RepositoryItemReaderBuilder<AuthorMongo>()
                .name("MongoReaderAuthors")
                .repository(authorRepositoryMongo)
                .methodName("findAll")
                .pageSize(100)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public RepositoryItemReader<GenreMongo> mongoItemReaderGenres() {
        LOGGER.info("Шаг ридера Жанров");
        return new RepositoryItemReaderBuilder<GenreMongo>()
                .name("MongoReaderGenres")
                .repository(genreRepositoryMongo)
                .methodName("findAll")
                .pageSize(100)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public RepositoryItemReader<BookMongo> mongoItemReaderBooks() {
        LOGGER.info("Шаг ридера Книг");
        return new RepositoryItemReaderBuilder<BookMongo>()
                .name("MongoReaderBooks")
                .repository(bookRepositoryMongo)
                .methodName("findAll")
                .pageSize(100)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Author> authorJdbcBatchItemWriter() {
        LOGGER.info("MongoWriterLog");
        JdbcBatchItemWriter<Author> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO authors (id, full_name) VALUES (:id, :fullName)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<Genre> genreJdbcBatchItemWriter() {
        LOGGER.info("Шаг вритера жанров");
        JdbcBatchItemWriter<Genre> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO genres (id, name) VALUES (:id, :name)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<BookUpdateDto> bookJdbcBatchItemWriter() {
        LOGGER.info("Шаг вритера книг");
        JdbcBatchItemWriter<BookUpdateDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO books (id, title, author_id, genre_id) VALUES (:id, :title, :authorId, :genreId)");
        writer.setDataSource(dataSource);
        LOGGER.info("проверка вритера книг");
        return writer;
    }
}
