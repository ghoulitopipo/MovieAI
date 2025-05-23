package fr.univtln.laure.service;

import org.apache.camel.builder.RouteBuilder;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import com.opencsv.CSVReader;

@ApplicationScoped
public class ImportDataCSV extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer://import?repeatCount=1")
            .routeId("import-all")
            // Import Movies
            .process(exchange -> {
                List<List<String>> movies = getCsvRows("src/main/resources/csv/movies.csv");
                for (List<String> row : movies) {
                    if (!row.get(0).equals("movieId")) {
                        String movieId = emptyToNull(row.get(0));
                        String title = row.size() > 1 ? "'" + row.get(1).replace("'", "''") + "'" : "NULL";
                        String genres = row.size() > 2 ? "'" + row.get(2).replace("'", "''") + "'" : "NULL";
                        String tmdb = (row.size() > 3 && row.get(3) != null && !row.get(3).trim().isEmpty()) ? row.get(3) : "NULL";
                        String query = String.format(
                            "INSERT INTO movie (id, title, genre, tmdb) VALUES (%s, %s, %s, %s) ON CONFLICT (id) DO NOTHING",
                            movieId, title, genres, tmdb
                        );
                        getContext().createProducerTemplate().sendBody("jdbc:dataSource", query);
                    }
                }
            }).log("Importation des movies terminée.")
            // Import Users
            .process(exchange -> {
                List<List<String>> users = getCsvRows("src/main/resources/csv/users.csv");
                for (List<String> row : users) {
                    if (!row.get(0).equals("username")) {
                        String username = row.size() > 0 ? "'" + row.get(0).replace("'", "''") + "'" : "NULL";
                        String email = row.size() > 1 ? "'" + row.get(1).replace("'", "''") + "'" : "NULL";
                        String password = row.size() > 2 ? "'" + row.get(2).replace("'", "''") + "'" : "NULL";
                        String query = String.format(
                            "INSERT INTO users (username, email, password) VALUES (%s, %s, %s) ON CONFLICT (id) DO NOTHING",
                            username, email, password
                        );
                        getContext().createProducerTemplate().sendBody("jdbc:dataSource", query);
                    }
                }
            }).log("Importation des users terminée.")
            // Import Tags
            .process(exchange -> {
                List<List<String>> tags = getCsvRows("src/main/resources/csv/tags.csv");
                for (List<String> row : tags) {
                    if (!row.get(0).equals("userId")) {
                        String userId = row.get(0);
                        String movieId = row.get(1);
                        String tag = row.size() > 2 ? "'" + row.get(2).replace("'", "''") + "'" : "NULL";
                        String date = row.get(3);
                        String query = String.format(
                            "INSERT INTO tag (user_id, movie_id, tag, date) VALUES (%s, %s, %s, %s) ON CONFLICT (id) DO NOTHING",
                            userId, movieId, tag, date
                        );
                        getContext().createProducerTemplate().sendBody("jdbc:dataSource", query);
                    }
                }
            })
            .log("Importation des tags terminée.")
            // Import Ratings
            .process(exchange -> {
                List<List<String>> ratings = getCsvRows("src/main/resources/csv/ratings.csv");
                for (List<String> row : ratings) {
                    if (!row.get(0).equals("userId")) {
                        String userId = row.get(0);
                        String movieId = row.get(1);
                        String rating = row.get(2);
                        String date = row.get(3);
                        String query = String.format(
                            "INSERT INTO rating (user_id, movie_id, rating, date) VALUES (%s, %s, %s, %s) ON CONFLICT (id) DO NOTHING",
                            userId, movieId, rating, date
                        );
                        getContext().createProducerTemplate().sendBody("jdbc:dataSource", query);
                    }
                }
            }).log("Importation des ratings terminée.");
    }
    
    private List<List<String>> getCsvRows(String path) throws Exception {
        try (java.io.Reader reader = new java.io.FileReader(path);
            CSVReader csvReader = new CSVReader(reader)) {
            List<List<String>> rows = new java.util.ArrayList<>();
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                rows.add(java.util.Arrays.asList(nextLine));
            }
            return rows;
        }
    }

    private String emptyToNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "NULL";
        }
        return value;
    }
}