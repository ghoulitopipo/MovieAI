package fr.univtln.laure.controller;

import fr.univtln.laure.model.Rating;
import fr.univtln.laure.model.Tag;
import fr.univtln.laure.service.TagService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/tags")
public class TagController {

    @Inject
    TagService tagService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @GET
    @Path("/csv")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Tag> getTagsFromCsv() {
        return tagService.readTagsFromCsv();
    }
}
