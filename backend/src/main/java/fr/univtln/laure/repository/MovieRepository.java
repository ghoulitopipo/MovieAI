package fr.univtln.laure.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import fr.univtln.laure.model.Movie;
import fr.univtln.laure.service.DPUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class MovieRepository{
    @PersistenceContext
    private EntityManager em;

    public List<Movie> findAllMovies() {
        // This method retrieves all movies from the database.
        return em.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
    }

    public Movie findById(Long id) {
        // This method retrieves a movie by its ID.
        return em.find(Movie.class, id);
    }

    public EntityManager getEntityManager() {
        return em;
    }


    public List<List<Object>> getListMoviesNotRated(long id_user, String genres) {
        /*
         * This method retrieves a list of movies that the user has not rated yet,
         * along with their average rating, genre, and associated tags.
         * It groups the results by movie ID, genre, and tag, and orders them by average rating in descending order.
         */
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
        /*
         * This method retrieves a list of movies that the user has rated,
         * along with their genre, rating, and associated tags.
         * It groups the results by genre and rating, and collects tags for each movie.
         */
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
            float privatizedRating = (float) DPUtils.privatizeRating(originalRating, 7.0);
            group.set(1, privatizedRating);

            ((List<String>) grouped.get(key).get(2)).add(tag);
        }

        List<List<Object>> finalResult = new ArrayList<>(grouped.values());
        return finalResult;

    }
    
    public List<String> getGenres() {
        // This method retrieves all unique genres from the Movie entity.
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
        // This method persists a Movie entity to the database.
        em.persist(movie);
    }
    
    public Movie merge(Movie movie) {
        // This method merges a Movie entity with the current persistence context.
        return em.merge(movie);
    }

    public void deleteAll() {
        // This method deletes all Movie entities from the database.
        em.createQuery("DELETE FROM Movie").executeUpdate();

    }

    public List<Movie> get8movies(int x) {
        return em.createQuery("SELECT m FROM Movie m", Movie.class)
                .setFirstResult(x)
                .setMaxResults(8)
                .getResultList();
    }

    public List<Movie> getMoviesByTitle(String title) {
        // This method retrieves movies by title, using a case-insensitive search.
        return em.createQuery("SELECT m FROM Movie m WHERE m.title ILIKE :title", Movie.class)
                .setParameter("title", "%" + title + "%")
                .getResultList();
    }

    public List<Long> getBestMoviesAverage() {
        // This method retrieves the IDs of the top 100 movies based on their average rating.
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
        // This method retrieves the IDs of the top 100 movies based on the count of ratings.
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

