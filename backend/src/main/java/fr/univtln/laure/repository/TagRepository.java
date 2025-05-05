package fr.univtln.laure.repository;

import fr.univtln.laure.model.Tag;

import jakarta.persistence.NoResultException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

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
}