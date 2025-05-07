package fr.univtln.laure.controller;

import fr.univtln.laure.model.Users;
import fr.univtln.laure.service.UsersService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/users")
public class UsersController {

    @Inject
    UsersService usersService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> getAllUsers() {
        return usersService.getAllUsers();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@FormParam("email") String email, @FormParam("password") String password) {
        Users user = usersService.authenticate(email, password);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(user).build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createUser(@FormParam("username") String username, @FormParam("email") String email, @FormParam("password") String password) {
        
        Users user = usersService.createUser(username, email, password);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(user).build();
    }

}
