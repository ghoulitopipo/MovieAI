package fr.univtln.laure.controller;

import fr.univtln.laure.model.Movie;
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
}

