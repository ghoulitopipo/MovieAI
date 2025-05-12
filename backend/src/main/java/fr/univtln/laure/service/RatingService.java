package fr.univtln.laure.service;

import fr.univtln.laure.model.Movie;
import fr.univtln.laure.model.Rating;
import fr.univtln.laure.model.Tag;
import fr.univtln.laure.model.Users;
import fr.univtln.laure.repository.MovieRepository;
import fr.univtln.laure.repository.UsersRepository;
import io.quarkus.runtime.StartupEvent;
import fr.univtln.laure.repository.RatingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RatingService {

    @Inject
    RatingRepository ratingRepository;

    public List<Rating> getAllRatings() {
        return ratingRepository.findAllRatings();
    }

    public List<Rating> readRatingsFromCsv() {
        List<Rating> ratings = new ArrayList<>();
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("ratings.csv");
            if (is == null) throw new RuntimeException("ratings.csv non trouvé dans resources");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }
                String[] parts = line.split(",", 4);
                if (parts.length == 4) {
                    Rating rating = new Rating();
                    Users user = new Users();
                    user.setId(Long.parseLong(parts[0]));
                    rating.setUsers(user);
                    Movie movie = new Movie();
                    movie.setId(Long.parseLong(parts[1]));
                    rating.setMovie(movie);
                    rating.setRating(Float.parseFloat(parts[2]));
                    long timestamp = Long.parseLong(parts[3]);
                    rating.setDate(Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDate());
                    ratings.add(rating);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ratings;
    }
}
