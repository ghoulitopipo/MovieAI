package fr.univtln.laure.repository;

import fr.univtln.laure.model.Movie;
import fr.univtln.laure.model.Rating;

import jakarta.persistence.NoResultException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

import fr.univtln.laure.model.Users;

@ApplicationScoped
public class RatingRepository {

    @PersistenceContext
    private EntityManager em;
    
    public List<Rating> findAllRatings() {
        // This method retrieves all ratings from the database.
        return em.createQuery("SELECT r FROM Rating r", Rating.class).getResultList();
    }

    public Rating findById(Long id) {
        // This method retrieves a rating by its ID.
        return em.find(Rating.class, id);
    }


    public float getaverageRating(long id_movie) {
        // This method calculates the average rating for a specific movie.
        // If there are no ratings, it returns 0.0f.
        // If there are no ratings for the movie, it returns -1.f.
        try {
            Double avg = em.createQuery(
                "SELECT AVG(r.rating) FROM Rating r WHERE r.movie.id = :id_movie", Double.class)
                .setParameter("id_movie", id_movie)
                .getSingleResult();

            return avg != null ? avg.floatValue() : 0.0f;
        } catch (NoResultException e) {
            return -1.f;
        }
    }
    
    public void persist(Rating rating) {
        // This method persists a new rating to the database.
        em.persist(rating);
    }
    
    public Rating merge(Rating rating) {
        // This method merges an existing rating with the current persistence context.
        return em.merge(rating);
    }

    public void deleteAll() {
        // This method deletes all ratings from the database.
        em.createQuery("DELETE FROM Rating").executeUpdate();

    }

    public Rating getRating(long id_movie, long id_user) {
        // This method retrieves a rating for a specific movie and user.
        try {
            return em.createQuery("SELECT r FROM Rating r WHERE r.movie.id = :id_movie AND r.user.id = :id_user", Rating.class)
                    .setParameter("id_movie", id_movie)
                    .setParameter("id_user", id_user)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public Rating addRating(long id_movie, long id_user, float rating) {
        // This method adds a new rating for a specific movie and user.
        // If a rating already exists for the movie and user, it modifies the existing rating.
        try {
            if (getRating(id_movie, id_user) != null) {
                return modifyRating(id_movie, id_user, rating); 
            } else{
                Movie movie = em.find(Movie.class, id_movie);
                Users user = em.find(Users.class, id_user);
                Rating newRating = new Rating();
                newRating.setMovie(movie);
                newRating.setUser(user);
                newRating.setRating(rating);
                newRating.setDate(LocalDate.now());
                em.persist(newRating);
                return newRating;
            }
        } catch (Exception e) {
            return null;
        }
    
        
    }

    @Transactional
    public Rating modifyRating(long id_movie, long id_user, float rating) {
        // This method modifies an existing rating for a specific movie and user.
        // If the new rating is the same as the existing one, it deletes the rating instead.
        try {
            Rating existingRating = getRating(id_movie, id_user);
            if (existingRating.getRating() != rating) {
                existingRating.setRating(rating);
                em.merge(existingRating);
                return existingRating;
            } else{
                deleteRating(id_movie, id_user);
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        
    }

    @Transactional
    public void deleteRating(long id_movie, long id_user) {
        // This method deletes a rating for a specific movie and user.
        Rating rating = getRating(id_movie, id_user);
        if (rating != null) {
            em.remove(rating);
        }
    }

    public int nbRatings(long id_user) {
        // This method retrieves the number of ratings made by a specific user.
        try {
            Long count = em.createQuery("SELECT COUNT(r) FROM Rating r WHERE r.user.id = :id_user", Long.class)
                    .setParameter("id_user", id_user)
                    .getSingleResult();
            return count.intValue();
        } catch (NoResultException e) {
            return 0;
        }
    }
}