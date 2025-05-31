package fr.univtln.laure.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class ApiMovies {
    /*
     * This class is used to interact with the ratings API.
     * It provides methods:
     * 
     * - ListNotRate(long id_user, String genre): to get the list of movies not rated by a user with a genre (return JSONArray)
     * 
     * - ListRated(long id_user, String genre): to get the list of movies rated by a user with a genre (return JSONArray)
     * 
     * - genres(): to get the list of genres (return List<String>)
     * 
     * - get8movies(int x): to get 8 movies (return JSONArray)
     * 
     * - getMoviebyID(long x): to get a movie by its ID (return JSONArray)
     * 
     * - getMoviebyTitle(String title): to get movies by title (return JSONArray)
     */
    private static final String BASE_URL = "http://localhost:8080"; // Base URL
    private static final HttpClient client = HttpClient.newHttpClient();

    public static List<String> genres() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/movies/genres"))
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

    public static JSONArray get8movies(int x) throws Exception {
        String url = String.format("%s/movies/8movies/%d", BASE_URL, x);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET() 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }

        JSONArray jsonArray = new JSONArray(response.body());
        return jsonArray;
    }

    public static JSONArray getMoviebyID(long x) throws Exception {
        String url = String.format("%s/movies/%d", BASE_URL, x);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET() 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }

        String body = response.body().trim();
        JSONArray jsonArray;
        if (body.startsWith("{")) {
            jsonArray = new JSONArray();
            jsonArray.put(new org.json.JSONObject(body));
        } else {
            jsonArray = new JSONArray(body);
        }
        return jsonArray;
    }

    public static JSONArray getMoviebyTitle(String title) throws Exception {
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8).replace("+", "%20");
        String url = String.format("%s/movies/title/%s", BASE_URL, encodedTitle);
        System.out.println("URL: " + url);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET() 
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }

        String body = response.body().trim();
        JSONArray jsonArray;
        if (body.startsWith("{")) {
            jsonArray = new JSONArray();
            jsonArray.put(new org.json.JSONObject(body));
        } else {
            jsonArray = new JSONArray(body);
        }
        return jsonArray;
    }
}