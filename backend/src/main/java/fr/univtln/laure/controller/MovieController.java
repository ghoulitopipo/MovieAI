package fr.univtln.laure.controller;

import fr.univtln.laure.model.Movie;
import fr.univtln.laure.service.MovieService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/movies")
public class MovieController {

    @Inject
    MovieService movieService;

    /* 
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    } */

    @GET
    @Path("/csv")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> getMoviesFromCsv() {
        return movieService.readMoviesFromCsv();
    }
}
