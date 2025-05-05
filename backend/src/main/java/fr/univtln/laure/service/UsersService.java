package fr.univtln.laure.service;

import fr.univtln.laure.model.Users;
import fr.univtln.laure.repository.UsersRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class UsersService {

    @Inject
    UsersRepository usersRepository;

    public List<Users> getAllUsers() {
        return usersRepository.findAllUsers();
    }
}
