package fr.univtln.laure.repository;

import fr.univtln.laure.model.Movie;
import fr.univtln.laure.model.Rating;

import jakarta.persistence.NoResultException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

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


    public List<Rating> findAllRatingsMovies(int id_movie) {
        try {
            return em.createQuery("SELECT r FROM Rating r WHERE r.movie.id = :id_movie", Rating.class)
                    .setParameter("id_movie", id_movie)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public float getRatingFloat(int id_movie, int id_user) {
        try {
            return em.createQuery("SELECT r.rating FROM Rating r WHERE r.movie.id = :id_movie AND r.user.id = :id_user", Float.class)
                    .setParameter("id_movie", id_movie)
                    .setParameter("id_user", id_user)
                    .getSingleResult();
        } catch (NoResultException e) {
            return -1.0f;
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
}