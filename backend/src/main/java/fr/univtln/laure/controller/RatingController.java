package fr.univtln.laure.controller;

import fr.univtln.laure.model.Rating;
import fr.univtln.laure.service.RatingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

import jakarta.ws.rs.core.Response;

@Path("/ratings")
public class RatingController {

    @Inject
    RatingService ratingService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRatings() {
        List<Rating> rate = ratingService.getAllRatings();
        if (rate == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(rate).build();
    }

    @GET
    @Path("/average/{id_movie}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getaverageRating(@PathParam("id_movie") long id_movie) {
        float rate = ratingService.getaverageRating(id_movie);
        if (rate == -1.f) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(rate).build();
    }

    @GET
    @Path("/get/{id_movie}/{id_user}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRating(@PathParam("id_movie") long id_movie, @PathParam("id_user") long id_user) {
        Rating rate = ratingService.getRating(id_movie, id_user);
        if (rate == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(rate).build();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addRating(@FormParam("id_movie") long id_movie, @FormParam("id_user") long id_user, @FormParam("rating") float rating) {
        Rating rate = ratingService.addRating(id_movie, id_user, rating);
        if (rate == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(rate).build();
    }

    @PUT
    @Path("/modify")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response modifyRating(@FormParam("id_movie") long id_movie, @FormParam("id_user") long id_user, @FormParam("rating") float rating) {
        Rating rate = ratingService.modifyRating(id_movie, id_user, rating);
        if (rate == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(rate).build();
    }
    
    @GET
    @Path("/nbRatings/{id_movie}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response nbRatings(@PathParam("id_movie") long id_movie) {
        int nb = ratingService.nbRatings(id_movie);
        if (nb == -1) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(nb).build();
    }
}
