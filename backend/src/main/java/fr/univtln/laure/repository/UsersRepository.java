package fr.univtln.laure.repository;

import fr.univtln.laure.model.Users;

import jakarta.persistence.NoResultException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.security.SecureRandom;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

@ApplicationScoped
public class UsersRepository {

    @PersistenceContext
    private EntityManager em;

    private static final int SALT_LENGTH = 16;
    
    public List<Users> findAllUsers() {
        return em.createQuery("SELECT u FROM Users u", Users.class).getResultList();
    }

    public Users findById(Long id) {
        return em.find(Users.class, id);
    }

    public Users findByEmailAndPassword(String email, String password) {
        try {
            Users user = em.createQuery("SELECT u FROM Users u WHERE u.email = :email", Users.class)
                    .setParameter("email", email)
                    .getSingleResult();
            
            if (user != null && checkPassword(password, user.getPassword())) {
                return user;
            }
            System.out.println("User not found");
            return null;

        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public Users addNewUser(String username, String email, String password) {
        try {
            em.createQuery("SELECT u FROM Users u WHERE u.email = :email", Users.class)
                    .setParameter("email", email)
                    .getSingleResult();

            System.out.println("Email already exist");
            return null;

        } catch (NoResultException e) {
            Users user = new Users();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(createStoredPassword(password));
                
            em.persist(user);
            return user;
        }
        
        
    }

    public static String hashPassword(String password, String salt) {
        String saltedPassword = password + salt;
        return DigestUtils.sha256Hex(saltedPassword);
    }

    public static String createStoredPassword(String password) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        return hashedPassword + ":" + salt;
    }

    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Hex.encodeHexString(salt);
    }

    private boolean checkPassword(String password, String storedPassword) {
        String[] parts = storedPassword.split(":");
        String hashedStoredPassword = parts[0]; 
        String salt = parts[1];

        String hashedPassword = hashPassword(password, salt);

        return hashedPassword.equals(hashedStoredPassword);
    }

}