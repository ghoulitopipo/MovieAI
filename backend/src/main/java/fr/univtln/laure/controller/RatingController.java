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
    public float getaverageRating(@PathParam("id_movie") long id_movie) {
        return ratingService.getaverageRating(id_movie);
    }

    @GET
    @Path("/getFloat/{id_movie}/{id_user}")
    @Produces(MediaType.APPLICATION_JSON)
    public float getRatingFloat(@PathParam("id_movie") long id_movie, @PathParam("id_user") long id_user) {
        return ratingService.getRatingFloat(id_movie, id_user);
    }

    @GET
    @Path("/get/{id_movie}/{id_user}")
    @Produces(MediaType.APPLICATION_JSON)
    public Rating getRating(@PathParam("id_movie") long id_movie, @PathParam("id_user") long id_user) {
        return ratingService.getRating(id_movie, id_user);
    }

    @POST
    @Path("/add/{id_movie}/{id_user}/{rating}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void addRating(@PathParam("id_movie") long id_movie, @PathParam("id_user") long id_user, @PathParam("rating") float rating) {
        ratingService.addRating(id_movie, id_user, rating);
    }

    @PUT
    @Path("/modify/{id_movie}/{id_user}/{rating}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void modifyRating(@PathParam("id_movie") long id_movie, @PathParam("id_user") long id_user, @PathParam("rating") float rating) {
        ratingService.modifyRating(id_movie, id_user, rating);
    }
    
}
