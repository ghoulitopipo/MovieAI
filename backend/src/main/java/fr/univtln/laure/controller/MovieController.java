package fr.univtln.laure.controller;

import java.util.List;

import fr.univtln.laure.model.Movie;
import fr.univtln.laure.service.MovieService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
        List<List<Object>> movies = movieService.getListMoviesNotRated(id_user, genre);
        if (movies == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(movies).build();
}

    @GET
    @Path("/rated/{id_user}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListMoviesRated(@PathParam("id_user") long id_user) {
        List<List<Object>> movies = movieService.getListMoviesRated(id_user);
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
        List<Movie> movie = movieService.get8movies(x);
        if (movie == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(movie).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieById(@PathParam("id") long id) {
        Movie movie = movieService.getMovieById(id);
        if (movie == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(movie).build();
    }

    @GET
    @Path("/title/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoviesByTitle(@PathParam("title") String title) {
        List<Movie> movies = movieService.getMoviesByTitle(title);
        if (movies == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(movies).build();
    }
}
