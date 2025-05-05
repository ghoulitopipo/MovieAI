package fr.univtln.laure.service;

import fr.univtln.laure.model.Rating;
import fr.univtln.laure.repository.RatingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class RatingService {

    @Inject
    RatingRepository ratingRepository;

    public List<Rating> getAllRatings() {
        return ratingRepository.findAllRatings();
    }
}
