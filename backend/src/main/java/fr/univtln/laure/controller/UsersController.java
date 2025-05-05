package fr.univtln.laure.controller;

import fr.univtln.laure.model.Users;
import fr.univtln.laure.service.UsersService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
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
}
