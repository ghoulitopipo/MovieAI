package fr.univtln.laure.service;

import fr.univtln.laure.model.Tag;
import fr.univtln.laure.repository.TagRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class TagService {

    @Inject
    TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAllTags();
    }
}
