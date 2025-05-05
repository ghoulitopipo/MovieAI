package fr.univtln.laure.service;

import fr.univtln.laure.model.Movie;
import fr.univtln.laure.repository.MovieRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class MovieService {

    @Inject
    MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAllMovies();
    }
}
