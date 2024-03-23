package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;


public interface RatingService {

    Rating save(Rating newRating);

    void saveMany(List<Rating> newRatings);

    void update(Rating rating);

    List<Rating> findAll();

    Rating findRatingByID(Integer ratingID);

    Rating findByName(String ratingName);

    boolean deleteById(Integer ratingID);

    boolean deleteAll();
}