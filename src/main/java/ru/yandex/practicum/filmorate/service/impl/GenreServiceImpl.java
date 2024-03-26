package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private GenreRepository genreRepository;


    @Override
    public Genre save(Genre genre) {
        Genre existingGenre = genreRepository.findByName(genre.getName());

        if (existingGenre != null) {
            throw new EntityAlreadyExistsException(User.class,
                    "Genre with name '" + genre.getName() + "' already exists");
        }

        return genreRepository.save(genre);
    }

    @Override
    public void saveMany(List<Genre> newGenres) {
        List<Genre> existingGenres = new ArrayList<>();
        List<Genre> newGenresToSave = new ArrayList<>();

        for (Genre newGenre : newGenres) {
            Integer existingGenreId = genreRepository.findIdByName(newGenre.getName());
            if (existingGenreId != null) {
                newGenre.setId(existingGenreId);
                existingGenres.add(newGenre);
            } else {
                newGenresToSave.add(newGenre);
            }
        }

        if (!newGenresToSave.isEmpty()) {
            genreRepository.saveMany(newGenresToSave);
        }
    }

    @Override
    public void update(Genre genre) {
        boolean isSuccess = genreRepository.update(genre);

        if (!isSuccess) {
            throw new EntityNotFoundException(Genre.class,
                    "Genre with id = " + genre.getId() + " hasn't been found");
        }
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Genre findGenreByID(Integer genreID) {
        try {
            return genreRepository.findGenreByID(genreID);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(Genre.class, "Genre with id = " + genreID + " hasn't been found");
        }
    }

    @Override
    public Genre findByName(String genreName) {
        if (genreRepository.findByName(genreName) == null) {
            throw new EntityNotFoundException(Genre.class, "Genre with name '" + genreName + "' hasn't been found");
        }

        return genreRepository.findByName(genreName);
    }

    @Override
    public boolean deleteById(Integer genreID) {
        return genreRepository.deleteById(genreID);
    }

    @Override
    public boolean deleteAll() {
        return genreRepository.deleteAll();
    }

}