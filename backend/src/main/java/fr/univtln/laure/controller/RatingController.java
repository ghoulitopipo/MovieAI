package fr.univtln.laure.controller;

import fr.univtln.laure.model.Rating;
import fr.univtln.laure.service.RatingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/ratings")
public class RatingController {

    @Inject
    RatingService ratingService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Rating> getAllRatings() {
        return ratingService.getAllRatings();
    }

    @GET
    @Path("/average/{id_movie}")
    @Produces(MediaType.APPLICATION_JSON)
    public float getaverageRating(@PathParam("id_movie") int id_movie) {
        return ratingService.getaverageRating(id_movie);
    }

    @GET
    @Path("/getFloat/{id_movie}/{id_user}")
    @Produces(MediaType.APPLICATION_JSON)
    public float getRatingFloat(@PathParam("id_movie") int id_movie, @PathParam("id_user") int id_user) {
        return ratingService.getRatingFloat(id_movie, id_user);
    }
}
