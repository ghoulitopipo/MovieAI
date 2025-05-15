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
    /*
     * This class is used to interact with the ratings API.
     * It provides methods:
     * 
     * - ListNotRate(long id_user, String genre): to get the list of movies not rated by a user with a genre (return JSONArray)
     * 
     * - ListRated(long id_user, String genre): to get the list of movies rated by a user with a genre (return JSONArray)
     * 
     * - genres(): to get the list of genres (return List<String>)
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
}
