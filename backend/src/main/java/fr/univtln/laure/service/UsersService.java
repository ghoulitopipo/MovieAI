package fr.univtln.laure.service;

import fr.univtln.laure.model.Users;
import fr.univtln.laure.repository.UsersRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import jakarta.transaction.Transactional;

@ApplicationScoped
public class UsersService {

    @Inject
    UsersRepository usersRepository;

    public List<Users> getAllUsers() {
        return usersRepository.findAllUsers();
    }

    public Users authenticate(String email, String password) {
        return usersRepository.findByEmailAndPassword(email, password);
    }

    @Transactional
    public Users createUser(String username, String email, String password) {
        return usersRepository.addNewUser(username, email, password);
    }

    public Long count(){
        return usersRepository.count();
    }
}
