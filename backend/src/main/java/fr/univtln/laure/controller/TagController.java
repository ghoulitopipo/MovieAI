package fr.univtln.laure.controller;

import fr.univtln.laure.model.Tag;
import fr.univtln.laure.service.TagService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
    @Path("/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addTag(@FormParam("id_movie") long id_movie, @FormParam("id_user") long id_user, @FormParam("tag") String tag) {
        Tag tagf = tagService.addTag(id_movie, id_user, tag);
        if (tagf == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(tagf).build();
    }

    @DELETE
    @Path("/delete/{id_movie}/{id_user}/{tag}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTag(@PathParam("id_movie") long id_movie, @PathParam("id_user") long id_user, @PathParam("tag") String tag) {
        Tag tagf = tagService.deleteTag(id_movie, id_user, tag);
        if (tagf == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(tagf).build();
    }

    @GET
    @Path("/get/{id_movie}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAll(@PathParam("id_movie") long id_movie) {
        List<Tag> tagf = tagService.getAll(id_movie);
        if (tagf == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(tagf).build();
    }

    @GET
    @Path("/get/{id_movie}/{id_user}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id_movie") long id_movie, @PathParam("id_movie") long id_user) {
        List<Tag> tagf = tagService.getUser(id_movie, id_user);
        if (tagf == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(tagf).build();
    }
}
