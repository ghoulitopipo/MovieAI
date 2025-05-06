package fr.univtln.laure.service;

import fr.univtln.laure.model.Movie;
import fr.univtln.laure.model.Tag;
import fr.univtln.laure.model.Users;
import fr.univtln.laure.repository.TagRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TagService {

    @Inject
    TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAllTags();
    }

    public List<Tag> readTagsFromCsv() {
    List<Tag> tags = new ArrayList<>();
    try {
        InputStream is = getClass().getClassLoader().getResourceAsStream("tags.csv");
        if (is == null) throw new RuntimeException("tags.csv non trouvé dans resources");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        boolean firstLine = true;
        while ((line = reader.readLine()) != null) {
            if (firstLine) { firstLine = false; continue; }
            String[] parts = line.split(",", 4);
            if (parts.length == 4) {
                Tag tag = new Tag();
                Users user = new Users();
                user.setId(Long.parseLong(parts[0]));
                tag.setUser(user);
                Movie movie = new Movie();
                movie.setId(Long.parseLong(parts[1]));
                tag.setMovie(movie);
                tag.setTag(parts[2]);
                long timestamp = Long.parseLong(parts[3]);
                tag.setDate(Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDate());
                tags.add(tag);
            }
        }
        reader.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return tags;
    }
}
