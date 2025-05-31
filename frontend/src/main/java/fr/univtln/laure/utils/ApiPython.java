package fr.univtln.laure.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;

public class ApiPython {
    /*
     * This class is used to interact with the pyhton recommendation API.
     * It provides methods:
     * 
     * - RecommendationForYou(long id_user): to get recommendations for the user (return JSONArray)
     * 
     * - RecommendationForOther(long id_user): to get recommendations for other users (return JSONArray)
     * 
     * - RecommendationForMovie(long id_movie): to get recommendations for a specific movie (return JSONArray)
     * 
     * - RecommendationForNoData(): to get recommendations when a user doesn't have enough ratings (return JSONArray)
     */
    private static final String BASE_URL = "http://localhost:5000";
    private static final HttpClient client = HttpClient.newHttpClient();

    
    public static JSONArray RecommendationForYou(long id_user) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/RecoForYou/" + id_user))
                .GET() 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }
        
        return new JSONArray(response.body());
    }

    public static JSONArray RecommendationForOther(long id_user) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/RecoByOther/" + id_user))
                .GET() 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }
        
        return new JSONArray(response.body());
    }

    public static JSONArray RecommendationForMovie(long id_movie) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/RecoForMovie/" + id_movie))
                .GET() 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }
        
        return new JSONArray(response.body());
    }

    public static JSONArray RecommendationForNoData() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/RecoNoData"))
                .GET() 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }
        
        return new JSONArray(response.body());
    }

    public static void update_values() throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/update_values"))
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();
    System.out.println("requete faite");

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() != 200) {
        throw new IOException("Request failed with status: " + response.statusCode());
    }
}
    
}
