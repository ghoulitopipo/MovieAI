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
        return em.createQuery("SELECT t FROM Tag t", Tag.class).getResultList();
    }

    public Tag findById(Long id) {
        return em.find(Tag.class, id);
    }

    public void persist(Tag tag) {
        em.persist(tag);
    }
    
    public Tag merge(Tag tag) {
        return em.merge(tag);
    }

    public void deleteAll() {
        em.createQuery("DELETE FROM Tag").executeUpdate();
    }

    @Transactional
    public void addTag(long id_movie, long id_user, String tag) {

        Movie movie = em.find(Movie.class, id_movie);
        Users user = em.find(Users.class, id_user);

        Tag tagEntity = new Tag();
        tagEntity.setMovie(movie);
        tagEntity.setUser(user);
        tagEntity.setTag(tag);
        tagEntity.setDate(LocalDate.now());
        em.persist(tagEntity);
    }

    @Transactional
    public void deleteTag(long id_movie, long id_user, String tag) {
        try {
            Tag tagEntity = em.createQuery("SELECT t FROM Tag t WHERE t.movie.id = :id_movie AND t.user.id = :id_user AND t.tag = :tag", Tag.class)
                    .setParameter("id_movie", id_movie)
                    .setParameter("id_user", id_user)
                    .setParameter("tag", tag)
                    .getSingleResult();
            em.remove(tagEntity);
        } catch (NoResultException e) {
        }
    }
}