package fr.univtln.laure.service;

import fr.univtln.laure.model.Movie;
import fr.univtln.laure.model.Rating;
import fr.univtln.laure.repository.RatingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RatingService {

    @Inject
    RatingRepository ratingRepository;

    public List<Rating> getAllRatings() {
        return ratingRepository.findAllRatings();
    }

    public float getaverageRating(int id_movie) {
        List<Rating> ratings = ratingRepository.findAllRatingsMovies(id_movie);
        float sum = 0;
        int count = 0;

        for (Rating rating : ratings) {
            if (rating.getMovie().getId() == id_movie) {
                sum += rating.getRating();
                count++;
            }
        }
 
        return sum / count;
    }

    public float getRatingFloat(int id_movie, int id_user) {
        return ratingRepository.getRatingFloat(id_movie, id_user);
    }
}
