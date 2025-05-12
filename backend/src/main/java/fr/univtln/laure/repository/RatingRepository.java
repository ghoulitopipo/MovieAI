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