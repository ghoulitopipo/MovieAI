package fr.univtln.laure.controller;

import fr.univtln.laure.model.Movie;
import fr.univtln.laure.service.MovieService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/movies")
public class MovieController {

    @Inject
    MovieService movieService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        if (movies == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(movies).build();
    }

    @GET
    @Path("/notrate/{id_user}/{genre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListMoviesNotRated(@PathParam("id_user") long id_user, @PathParam("genre") String genre) {
        List<Movie> movies = movieService.getListMoviesNotRated(id_user, genre);
        if (movies == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(movies).build();
}

    @GET
    @Path("/rated/{id_user}/{genre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListMoviesRated(@PathParam("id_user") long id_user, @PathParam("genre") String genre) {
        List<Movie> movies = movieService.getListMoviesRated(id_user, genre);
        if (movies == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(movies).build();
    }

    @GET
    @Path("/genres")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenres() {
        List<String> genres = movieService.getGenres();
        if (genres == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(genres).build();
    }

    @GET
    @Path("/8movies/{x}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get8movies(@PathParam("x") int x) {
        List<String> movie = movieService.get8movies(x);
        if (movie == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(movie).build();
    }
}
