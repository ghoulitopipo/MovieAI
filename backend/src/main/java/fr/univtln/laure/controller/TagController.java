package fr.univtln.laure.controller;

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

    @POST
    @Path("/add/{id_movie}/{id_user}/{tag}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void addTag(@PathParam("id_movie") long id_movie, @PathParam("id_user") long id_user, @PathParam("tag") String tag) {
        tagService.addTag(id_movie, id_user, tag);
    }

    @DELETE
    @Path("/delete/{id_movie}/{id_user}/{tag}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteTag(@PathParam("id_movie") long id_movie, @PathParam("id_user") long id_user, @PathParam("tag") String tag) {
        tagService.deleteTag(id_movie, id_user, tag);
    }
}
