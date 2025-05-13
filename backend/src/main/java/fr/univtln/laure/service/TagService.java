package fr.univtln.laure.service;

import fr.univtln.laure.model.Tag;
import fr.univtln.laure.repository.TagRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class TagService {

    @Inject
    TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAllTags();
    }

    @Transactional
    public void addTag(long id_movie, long id_user, String tag) {
        tagRepository.addTag(id_movie, id_user, tag);
    }

    @Transactional
    public void deleteTag(long id_movie, long id_user, String tag) {
        tagRepository.deleteTag(id_movie, id_user, tag);
    }
}
