package fr.univtln.laure.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import fr.univtln.laure.model.Movie;
import fr.univtln.laure.service.DPUtils;

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


    public List<List<Object>> getListMoviesNotRated(long id_user, String genres) {
        List<Object[]> rows = em.createQuery(
                    "SELECT m.id, " +
                    "    AVG(r.rating) as avgRating, " +
                    "   m.genre, t.tag " +
                    "FROM Movie m " +
                    "JOIN Rating r ON r.movie.id = m.id " +
                    "JOIN Tag t ON t.movie.id = m.id " +
                    "WHERE m.genre LIKE :genre " +
                    "AND m.id NOT IN ( " +
                    "   SELECT r2.movie.id FROM Rating r2 WHERE r2.user.id = :id_user) " +
                    "GROUP BY m.id, m.genre, t.tag " +
                    "ORDER BY avgRating DESC "
                , Object[].class)
                .setParameter("genre", "%" + genres + "%")
                .setParameter("id_user", id_user)
                .setMaxResults(20)
                .getResultList();
            
            Map<String, List<Object>> grouped = new LinkedHashMap<>();

            for (Object[] row : rows) {
                Long id = (Long) row[0];
                Number ratingNum = (Number) row[1];
                float average = ratingNum.floatValue();
                String genre = (String) row[2];
                String tag = (String) row[3];

                String key = genre + "::" + average;

                if (!grouped.containsKey(id)) {

                    List<Object> entry = new ArrayList<>();
                    entry.add(id);             
                    entry.add(average);    
                    entry.add(genre);       
                    entry.add(new ArrayList<>());
                    grouped.put(key, entry);
                }
    

    
                ((List<String>) grouped.get(key).get(3)).add(tag);
            }
    
            List<List<Object>> finalResult = new ArrayList<>(grouped.values());
            return finalResult;

    }

    public List<List<Object>> getListMoviesRated(long id_user) {
        List<Object[]> rows = em.createQuery(
                            "SELECT m.genre, r.rating, t.tag " +
                            "FROM Rating r " +
                            "JOIN r.movie m " +
                            "JOIN Tag t ON t.movie.id = m.id " +
                            "WHERE r.user.id = :id_user ", Object[].class)
                        .setParameter("id_user", id_user)
                        .getResultList();
        
        Map<String, List<Object>> grouped = new LinkedHashMap<>();

        for (Object[] row : rows) {
            String genre = (String) row[0];
            Number ratingNum = (Number) row[1];
            float rating = ratingNum.floatValue();
            String tag = (String) row[2];

            String key = genre + "::" + rating;

            if (!grouped.containsKey(key)) {

                List<Object> entry = new ArrayList<>();
                entry.add(genre);             
                entry.add(rating);           
                entry.add(new ArrayList<>());
                grouped.put(key, entry);
            }

            List<Object> group = grouped.get(key);
            float originalRating = (float) group.get(1);
            float privatizedRating = (float) DPUtils.privatizeRating(originalRating, 5.0);
            group.set(1, privatizedRating);

            ((List<String>) grouped.get(key).get(2)).add(tag);
        }

        List<List<Object>> finalResult = new ArrayList<>(grouped.values());
        return finalResult;

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

    public List<Long> getBestMoviesAverage() {
        List<Long> bestMovies = em.createQuery(
                "SELECT m.id FROM Movie m " +
                "JOIN Rating r ON r.movie.id = m.id " +
                "GROUP BY m.id " +
                "ORDER BY AVG(r.rating) DESC", Long.class)
                .setMaxResults(100)
                .getResultList();
        return bestMovies;
    }

    public List<Long> getBestMoviesCount() {
        List<Long> bestMovies = em.createQuery(
                "SELECT m.id FROM Movie m " +
                "JOIN Rating r ON r.movie.id = m.id " +
                "GROUP BY m.id " +
                "ORDER BY count(r.rating) DESC", Long.class)
                .setMaxResults(100)
                .getResultList();
        return bestMovies;
    }

}

