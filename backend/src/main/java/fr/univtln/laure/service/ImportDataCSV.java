package fr.univtln.laure.service;

import org.apache.camel.builder.RouteBuilder;
import java.util.List;

//@ApplicationScoped
public class ImportDataCSV {
/*
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
                        String query = String.format(
                            "INSERT INTO movie (id, title, genre) VALUES (%s, %s, %s) ON CONFLICT (id) DO NOTHING",
                            movieId, title, genres
                        );
                        getContext().createProducerTemplate().sendBody("jdbc:dataSource", query);
                    }
                }
            })
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
            })
            // Import Ratings
            .process(exchange -> {
                List<List<String>> ratings = getCsvRows("src/main/resources/csv/ratings.csv");
                for (List<String> row : ratings) {
                    if (!row.get(0).equals("userId")) {
                        String userId = emptyToNull(row.get(0));
                        String movieId = emptyToNull(row.get(1));
                        String rating = emptyToNull(row.get(2));
                        String date = emptyToNull(row.size() > 3 ? row.get(3) : null);
                        String query = String.format(
                            "INSERT INTO rating (user_id, movie_id, rating, date) VALUES (%s, %s, %s, %s) ON CONFLICT (id) DO NOTHING",
                            userId, movieId, rating, date
                        );
                        getContext().createProducerTemplate().sendBody("jdbc:dataSource", query);
                    }
                }
            })
            // Import Tags
            .process(exchange -> {
                List<List<String>> tags = getCsvRows("src/main/resources/csv/tags.csv");
                for (List<String> row : tags) {
                    if (!row.get(0).equals("userId")) {
                        String userId = emptyToNull(row.get(0));
                        String movieId = emptyToNull(row.get(1));
                        String tag = row.size() > 2 ? "'" + row.get(2).replace("'", "''") + "'" : "NULL";
                        String date = emptyToNull(row.size() > 3 ? row.get(3) : null);
                        String query = String.format(
                            "INSERT INTO tag (user_id, movie_id, tag, date) VALUES (%s, %s, %s, %s) ON CONFLICT (id) DO NOTHING",
                            userId, movieId, tag, date
                        );
                        getContext().createProducerTemplate().sendBody("jdbc:dataSource", query);
                    }
                }
            })
            .log("Importation des données terminée.");
    }
    
    private List<List<String>> getCsvRows(String path) throws Exception {
        try (java.io.InputStream is = new java.io.FileInputStream(path);
             java.util.Scanner scanner = new java.util.Scanner(is)) {
            java.util.List<List<String>> rows = new java.util.ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Simple split, à adapter si tu as des virgules dans les champs
                java.util.List<String> row = java.util.Arrays.asList(line.split(","));
                rows.add(row);
            }
            return rows;
        }
    }

    private String emptyToNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "NULL";
        }
        return value;
    } */
}