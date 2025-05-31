package fr.univtln.laure.service;

import fr.univtln.laure.model.Rating;
import fr.univtln.laure.repository.RatingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import jakarta.transaction.Transactional;

@ApplicationScoped
public class RatingService {

    @Inject
    RatingRepository ratingRepository;

    public List<Rating> getAllRatings() {
        List<Rating> ratings = ratingRepository.findAllRatings();
        for (Rating rating : ratings) {
            rating.setRating((float) DPUtils.privatizeRating(rating.getRating(), 7.0));
        }
        return ratings;
    }

    public float getaverageRating(long id_movie) {
        return ratingRepository.getaverageRating(id_movie);
    }

    public Rating getRating(long id_movie, long id_user) {
        Rating rating = ratingRepository.getRating(id_movie, id_user);
        if (rating != null) {
            rating.setRating((float) DPUtils.privatizeRating(rating.getRating(), 7.0));
        }
        return rating; 
    }

    @Transactional
    public Rating addRating(long id_movie, long id_user, float rating) {
        return ratingRepository.addRating(id_movie, id_user, rating);
    }

    @Transactional
    public Rating modifyRating(long id_movie, long id_user, float rating) {
        return ratingRepository.modifyRating(id_movie, id_user, rating);
    }

    public int nbRatings(long id_movie) {
        return ratingRepository.nbRatings(id_movie);
    }
}
