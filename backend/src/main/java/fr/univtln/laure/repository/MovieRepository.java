package fr.univtln.laure.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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


    public List<Movie> getListMoviesNotRated(long id_user, String genre) {
        genre = "%" + genre + "%";
        return em.createQuery("SELECT m FROM Movie m WHERE m.genre LIKE :genre AND m.id NOT IN (SELECT r.movie.id FROM Rating r WHERE r.user.id = :id_user)", Movie.class)
                .setParameter("id_user", id_user)
                .setParameter("genre", genre)
                .getResultList();
    }

    public List<Movie> getListMoviesRated(long id_user, String genre) {
        genre = "%" + genre + "%";
        return em.createQuery("SELECT m FROM Movie m WHERE m.genre LIKE :genre AND m.id IN (SELECT r.movie.id FROM Rating r WHERE r.user.id = :id_user)", Movie.class)
                .setParameter("id_user", id_user)
                .setParameter("genre", genre)
                .getResultList();
    }
    
    public List<String> getGenres() {
        List<String> badgenres = em.createQuery("SELECT DISTINCT m.genre FROM Movie m", String.class).getResultList();
        
        Set<String> uniqueGenres = new TreeSet<>(); 

        for (String genres : badgenres) {
            String[] splitGenres = genres.split("\\|");
            uniqueGenres.addAll(Arrays.asList(splitGenres));
        }

        for (String genre : uniqueGenres) {
            System.out.println(genre);
        }

        return new ArrayList<>(uniqueGenres);
    }

    public void persist(Movie movie) {
        em.persist(movie);
    }
    
    public Movie merge(Movie movie) {
        return em.merge(movie);
    }

    public void deleteAll() {
        em.createQuery("DELETE FROM Movie").executeUpdate();

    }

    public List<Movie> get8movies(int x) {
        return em.createQuery("SELECT m FROM Movie m", Movie.class)
                .setFirstResult(x)
                .setMaxResults(8)
                .getResultList();
    }

    public List<Movie> getMoviesByTitle(String title) {
        return em.createQuery("SELECT m FROM Movie m WHERE m.title ILIKE :title", Movie.class)
                .setParameter("title", "%" + title + "%")
                .getResultList();
    }
}

