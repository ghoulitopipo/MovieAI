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
        // This method retrieves all users from the database.
        return em.createQuery("SELECT u FROM Users u", Users.class).getResultList();
    }

    public Users findById(Long id) {
        // This method retrieves a user by their ID.
        return em.find(Users.class, id);
    }

    public Users findByEmailAndPassword(String email, String password) {
        // This method retrieves a user by their email and password.
        // It checks if the user exists and if the provided password matches the stored password.
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
        // This method adds a new user to the database.
        // It checks if the email already exists, and if not, it creates a new user with the provided details.
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
        // This method hashes the password using SHA-256 with a salt.
        // The salt is appended to the password before hashing to enhance security.
        String saltedPassword = password + salt;
        return DigestUtils.sha256Hex(saltedPassword);
    }

    public static String createStoredPassword(String password) {
        // This method creates a stored password by generating a salt and hashing the password with it.
        // The stored password is a combination of the hashed password and the salt, separated by a colon.
        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        return hashedPassword + ":" + salt;
    }

    private static String generateSalt() {
        // This method generates a random salt using SecureRandom.
        // The salt is a byte array of a specified length, which is then converted to a hexadecimal string.
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Hex.encodeHexString(salt);
    }

    private boolean checkPassword(String password, String storedPassword) {
        // This method checks if the provided password matches the stored password.
        // It splits the stored password to retrieve the hashed password and the salt, then hashes the provided password with the same salt.
        String[] parts = storedPassword.split(":");
        String hashedStoredPassword = parts[0]; 
        String salt = parts[1];
        String hashedPassword = hashPassword(password, salt);

        return hashedPassword.equals(hashedStoredPassword);
    }

    public Long count(){
        // This method counts the total number of users in the database.
        return em.createQuery(
            "SELECT COUNT(u) FROM Users u", Long.class)
            .getSingleResult();
    }

}