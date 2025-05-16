package fr.univtln.laure.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;

public class ApiTags {
    /*
     * This class is used to interact with the ratings API.
     * It provides methods:
     * 
     * - addTag(long id_movie, long id_user, String tag): to add a tag by a user (return void)
     *
     * - deleteTags(long id_movie, long id_user, String tag): to delete a tag by a user (return void)
     *
     * - getAll(long id_movie): to get all tags for a movie (return JSONArray)
     * 
     * - getUser(long id_movie, long id_user): to get all tags for a movie by a user (return JSONArray)
     */
    private static final String BASE_URL = "http://localhost:8080"; // Base URL
    private static final HttpClient client = HttpClient.newHttpClient();

    public void addTag(long id_movie, long id_user, String tag) throws Exception {

        String form = "id_movie=" + id_movie + "&id_user=" + id_user + "&tag=" + tag;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/tags/add"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form)) 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }
    
        
    }

    public void deleteTags(long id_movie, long id_user, String tag) throws Exception {
        String url = String.format("%s/tags/delete/%d/%d/%f", BASE_URL, id_movie, id_user, tag);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }
    }

    public static JSONArray getAll(long id_movie) throws Exception {
        String url = String.format("%s/tags/get/%d", BASE_URL, id_movie);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET() 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }

        return new JSONArray(response.body());
    }
    
    public JSONArray getUser(long id_movie, long id_user) throws Exception {
        String url = String.format("%s/tags/get/%d/%d", BASE_URL, id_movie, id_user);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET() 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }

        return new JSONArray(response.body());
    }
}
