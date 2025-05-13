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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GET
    @Path("/notrate/{id_user}/{genre}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> getListMoviesNotRated(@PathParam("id_user") long id_user, @PathParam("genre") String genre) {
        return movieService.getListMoviesNotRated(id_user, genre);
}

    @GET
    @Path("/rated/{id_user}/{genre}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> getListMoviesRated(@PathParam("id_user") long id_user, @PathParam("genre") String genre) {
        return movieService.getListMoviesRated(id_user, genre);
    }

    @GET
    @Path("/genres")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getGenres() {
        return movieService.getGenres();
    }
}
