package fr.univtln.laure.repository;

import fr.univtln.laure.model.Users;

import jakarta.persistence.NoResultException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class UsersRepository {

    @PersistenceContext
    private EntityManager em;
    
    public List<Users> findAllUsers() {
        return em.createQuery("SELECT u FROM Users u", Users.class).getResultList();
    }

    public Users findById(Long id) {
        return em.find(Users.class, id);
    }

    public Users findByEmailAndPassword(String email, String password) {
        try {
            Users user = em.createQuery("SELECT u FROM Users u WHERE u.email = :email AND u.password = :password", Users.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult();
            
            if (user != null) {
                return user;
            }
            System.out.println("User not found");
            return null;

        } catch (NoResultException e) {
            return null;
        }
    }

    public Users addNewUser(String username, String email, String password) {
        
        Users user = new Users();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        
        em.persist(user);
        return user;
    }

}