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

    public List<List<Object>> getListMoviesNotRated(long id_user, String genre) {
        return movieRepository.getListMoviesNotRated(id_user, genre);
    }

    public List<List<Object>> getListMoviesRated(long id_user) {
        return movieRepository.getListMoviesRated(id_user);
    }
    
    public List<String> getGenres() {
        return movieRepository.getGenres();
    }

        public List<Movie> get8movies(int x) {
        return movieRepository.get8movies(x);
    }

    public Movie getMovieById(long id) {
        return movieRepository.findById(id);
    }

    public List<Movie> getMoviesByTitle(String title) {
        return movieRepository.getMoviesByTitle(title);
    }

    public List<Long> getBestMoviesAverage() {
        return movieRepository.getBestMoviesAverage();
    }

    public List<Long> getBestMoviesCount() {
        return movieRepository.getBestMoviesCount();
    }


}
