package fr.univtln.laure.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

public class ApiRatings {
    private static final String BASE_URL = "http://localhost:8080"; // Base URL
    private static final HttpClient client = HttpClient.newHttpClient();


    public static float AverageRating(int id_movie) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/ratings/average/" + id_movie))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .GET() 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        

        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }
        return Float.parseFloat(response.body());
    }

    public static Map<String, Object> AverageRatingAndCount(JSONArray movies, int id_user) throws Exception {
        float total = 0f;
        int count = 0;

        for (int i = 0; i < movies.length(); i++) {
            int id_movie = movies.getInt(i);

            String url = String.format("%s/ratings/getFloat/%d/%d", BASE_URL, id_movie, id_user);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                float rating = Float.parseFloat(response.body());
                total += rating;
                count++;
            } else {
                System.err.println("Failed to get rating for movie ID " + id_movie);
            }
        }

        if (count == 0) {
            throw new IllegalStateException("No valid responses received.");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("average", total / count);
        result.put("count", count);

        return result;
    }
}
