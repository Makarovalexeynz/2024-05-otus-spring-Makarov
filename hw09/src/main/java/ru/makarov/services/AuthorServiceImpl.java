package ru.makarov.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.makarov.dto.AuthorDto;
import ru.makarov.mappers.AuthorMapper;
import ru.makarov.repositories.AuthorRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream().map(authorMapper::toDto).toList();
    }
}
