package fr.univtln.laure.service;

import fr.univtln.laure.model.Movie;
import fr.univtln.laure.repository.MovieRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MovieService {

    @Inject
    MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAllMovies();
    }

    public List<Movie> readMoviesFromCsv() {
    List<Movie> movies = new ArrayList<>();
    try {
        InputStream is = getClass().getClassLoader().getResourceAsStream("movies.csv");
        if (is == null) {
            throw new RuntimeException("movies.csv non trouvé dans resources");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        boolean firstLine = true;
        while ((line = reader.readLine()) != null) {
            if (firstLine) { // ignorer l'en-tête
                firstLine = false;
                continue;
            }
            String[] parts = line.split(",", 3);
            if (parts.length == 3) {
                Movie movie = new Movie();
                movie.setId(Long.parseLong(parts[0]));
                movie.setTitle(parts[1]);
                movie.setGenre(parts[2]);
                movies.add(movie);
            }
        }
        reader.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return movies;
}
}
