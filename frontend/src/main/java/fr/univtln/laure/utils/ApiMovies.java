package fr.univtln.laure.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class ApiMovies {
    private static final String BASE_URL = "http://localhost:8080"; // Base URL
    private static final HttpClient client = HttpClient.newHttpClient();


    public static JSONArray ListNotRate(int id_user, String genre) throws Exception {

        String url = String.format("%s/movies/notrate/%d/%s", BASE_URL, id_user, genre);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .GET() 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }
        return new JSONArray(response.body());
    }

    public static JSONArray ListRated(int id_user, String genre) throws Exception {

        String url = String.format("%s/movies/rated/%d/%s", BASE_URL, id_user, genre);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .GET() 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }
        return new JSONArray(response.body());
    }

    public static List<String> genres() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/movies/genres"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .GET() 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }

        JSONArray jsonArray = new JSONArray(response.body());
        List<String> genres = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            genres.add(jsonArray.getString(i));
        }

        return genres;
    }
}
