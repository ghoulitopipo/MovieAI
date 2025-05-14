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
import fr.univtln.laure.service.UsersService;
import io.quarkus.security.User;

@ApplicationScoped
public class RatingRepository {

    @PersistenceContext
    private EntityManager em;
    
    public List<Rating> findAllRatings() {
        return em.createQuery("SELECT r FROM Rating r", Rating.class).getResultList();
    }

    public Rating findById(Long id) {
        return em.find(Rating.class, id);
    }


    public List<Rating> findAllRatingsMovies(long id_movie) {
        try {
            return em.createQuery("SELECT r FROM Rating r WHERE r.movie.id = :id_movie", Rating.class)
                    .setParameter("id_movie", id_movie)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public void persist(Rating rating) {
        em.persist(rating);
    }
    
    public Rating merge(Rating rating) {
        return em.merge(rating);
    }

    public void deleteAll() {
        em.createQuery("DELETE FROM Rating").executeUpdate();

    }

    public Rating getRating(long id_movie, long id_user) {
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
    public void deleteRating(long id_movie, long id_user) {
        Rating rating = getRating(id_movie, id_user);
        if (rating != null) {
            em.remove(rating);
        }
    }

    @Transactional
    public Rating modifyRating(long id_movie, long id_user, float rating) {
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
    
}