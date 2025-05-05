package fr.univtln.laure.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.List;
import fr.univtln.laure.model.Movie;

@ApplicationScoped
public class MovieRepository{
    @PersistenceContext
    private EntityManager em;

    public List<Movie> findAllMovies() {
        return em.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
    }

    public Movie findById(Long id) {
        return em.find(Movie.class, id);
    }

    public EntityManager getEntityManager() {
        return em;
    }
}