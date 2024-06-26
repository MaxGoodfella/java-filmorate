package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.RatingRepository;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@AllArgsConstructor
@Service
public class RatingServiceImpl implements RatingService {

    private RatingRepository ratingRepository;


    @Override
    public Rating save(Rating rating) {
        Rating existingRating = ratingRepository.findByName(rating.getName());

        if (existingRating != null) {
            throw new EntityAlreadyExistsException(User.class,
                    "Rating with name '" + rating.getName() + "' already exists");
        }

        return ratingRepository.save(rating);
    }

    @Override
    public void update(Rating rating) {
        boolean isSuccess = ratingRepository.update(rating);

        if (!isSuccess) {
            throw new EntityNotFoundException(Rating.class,
                    "Rating with id = " + rating.getId() + " hasn't been found");
        }
    }

    @Override
    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    @Override
    public Rating findByID(Integer ratingID) {
        if (ratingRepository.findByID(ratingID) == null) {
            throw new EntityNotFoundException(Rating.class, "Rating with id = " + ratingID + " hasn't been found");
        }

        return ratingRepository.findByID(ratingID);
    }

    @Override
    public Rating findByName(String ratingName) {
        if (ratingRepository.findByName(ratingName) == null) {
            throw new EntityNotFoundException(Rating.class, "Rating with name '" + ratingName + "' hasn't been found");
        }

        return ratingRepository.findByName(ratingName);
    }

    @Override
    public boolean deleteById(Integer ratingID) {
        return ratingRepository.deleteById(ratingID);
    }

    @Override
    public boolean deleteAll() {
        return ratingRepository.deleteAll();
    }

}