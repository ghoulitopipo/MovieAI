package fr.univtln.laure.repository;

import fr.univtln.laure.model.Users;

import jakarta.persistence.NoResultException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.binary.Hex;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.security.SecureRandom;
import java.util.List;

@ApplicationScoped
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    private static final int SALT_LENGTH = 16;

    // Récupérer tous les users
    public List<Users> findAllUsers() {
        return em.createQuery("SELECT u FROM User u", Users.class).getResultList();
    }

    // Trouver un user par son ID
    public Users findById(Long id) {
        return em.find(Users.class, id);
    }


    public Users findByEmailAndPassword(String email, String password) {
        try {
            // Récupérer l'enseignant par email
            Users user = em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password", Users.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult();
            return user;
        } catch (NoResultException e) {
            return null;
        }
    }
}