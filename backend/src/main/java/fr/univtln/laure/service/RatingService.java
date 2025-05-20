package fr.univtln.laure.service;

import fr.univtln.laure.model.Movie;
import fr.univtln.laure.model.Rating;
import fr.univtln.laure.repository.RatingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.beans.Transient;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

@ApplicationScoped
public class RatingService {

    @Inject
    RatingRepository ratingRepository;

    public List<Rating> getAllRatings() {
        return ratingRepository.findAllRatings();
    }

    public float getaverageRating(long id_movie) {
        return ratingRepository.getaverageRating(id_movie);
    }

    public Rating getRating(long id_movie, long id_user) {
        return ratingRepository.getRating(id_movie, id_user);
    }

    @Transactional
    public Rating addRating(long id_movie, long id_user, float rating) {
        return ratingRepository.addRating(id_movie, id_user, rating);
    }

    @Transactional
    public Rating modifyRating(long id_movie, long id_user, float rating) {
        return ratingRepository.modifyRating(id_movie, id_user, rating);
    }
}
