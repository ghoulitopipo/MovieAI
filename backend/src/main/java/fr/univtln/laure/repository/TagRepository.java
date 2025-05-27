package fr.univtln.laure.repository;

import fr.univtln.laure.model.Movie;
import fr.univtln.laure.model.Users;
import fr.univtln.laure.model.Tag;

import jakarta.persistence.NoResultException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class TagRepository {

    @PersistenceContext
    private EntityManager em;
    
    public List<Tag> findAllTags() {
        // This method retrieves all tags from the database.
        return em.createQuery("SELECT t FROM Tag t", Tag.class).getResultList();
    }

    public Tag findById(Long id) {
        // This method retrieves a tag by its ID.
        return em.find(Tag.class, id);
    }

    public void persist(Tag tag) {
        // This method persists a new tag to the database.
        em.persist(tag);
    }
    
    public Tag merge(Tag tag) {
        // This method merges the state of the given tag into the current persistence context.
        return em.merge(tag);
    }

    public void deleteAll() {
        // This method deletes all tags from the database.
        em.createQuery("DELETE FROM Tag").executeUpdate();
    }

    @Transactional
    public Tag addTag(long id_movie, long id_user, String tag) {
        // This method adds a new tag to a movie by a user.
        try {
            Movie movie = em.find(Movie.class, id_movie);
            Users user = em.find(Users.class, id_user);

            Tag tagEntity = new Tag();
            tagEntity.setMovie(movie);
            tagEntity.setUser(user);
            tagEntity.setTag(tag);
            tagEntity.setDate(LocalDate.now());
            em.persist(tagEntity);
            return tagEntity;
        } catch (Exception e) {
            return null;
        }
        
    }

    @Transactional
    public Tag deleteTag(long id_movie, long id_user, String tag) {
        // This method deletes a tag from a movie by a user.
        try {
            Tag tagEntity = em.createQuery("SELECT t FROM Tag t WHERE t.movie.id = :id_movie AND t.user.id = :id_user AND t.tag = :tag", Tag.class)
                    .setParameter("id_movie", id_movie)
                    .setParameter("id_user", id_user)
                    .setParameter("tag", tag)
                    .getSingleResult();
            em.remove(tagEntity);
            return tagEntity;
        } catch (NoResultException e) {
            return null;
        }

    }

    public List<Tag> getAll(long id_movie) {
        // This method retrieves all tags for a specific movie.
        try {
            return em.createQuery("SELECT t FROM Tag t WHERE t.movie.id = :id_movie", Tag.class)
                .setParameter("id_movie", id_movie)
                .getResultList();
        } catch (Exception e) {
            return null;
        }
        
    }

    public List<Tag> getUser(long id_movie, long id_user) {
        // This method retrieves all tags for a specific movie by a specific user.
        try{
            return em.createQuery("SELECT t FROM Tag t WHERE t.movie.id = :id_movie AND t.user.id = :id_user", Tag.class)
                .setParameter("id_movie", id_movie)
                .setParameter("id_user", id_user)
                .getResultList();
        } catch (Exception e) {
            return null;
        }
        
    }
}