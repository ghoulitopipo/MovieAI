package fr.univtln.laure.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;

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


    public List<Movie> getListMoviesNotRated(int id_user, String genre) {
        genre = "%" + genre + "%";
        return em.createQuery("SELECT m FROM Movie m WHERE m.genre LIKE :genre AND m.id NOT IN (SELECT r.movie.id FROM Rating r WHERE r.user.id = :id_user)", Movie.class)
                .setParameter("id_user", id_user)
                .setParameter("genre", genre)
                .getResultList();
    }

    public List<Movie> getListMoviesRated(int id_user, String genre) {
        return em.createQuery("SELECT m FROM Movie m WHERE m.genre = :genre AND m.id IN (SELECT r.movie.id FROM Rating r WHERE r.user.id = :id_user)", Movie.class)
                .setParameter("id_user", id_user)
                .setParameter("genre", genre)
                .getResultList();
    }
    
    public List<String> getGenres() {
        return em.createQuery("SELECT DISTINCT m.genre FROM Movie m", String.class).getResultList();

    public void persist(Movie movie) {
        em.persist(movie);
    }
    
    public Movie merge(Movie movie) {
        return em.merge(movie);
    }

    public void deleteAll() {
        em.createQuery("DELETE FROM Movie").executeUpdate();

    }
}

