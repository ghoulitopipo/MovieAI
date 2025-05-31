package fr.univtln.laure.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class ApiRatings {
    /*
     * This class is used to interact with the ratings API.
     * It provides methods:
     * 
     * - getRating(long id_movie, long id_user): to get the rating of a movie by a user (return float)
     * 
     * - averageRating(long id_movie): to get the average rating of a movie by all users (return float)
     * 
     * - averageRatingAndCount(JSONArray movies, long id_user): to get the average rating and count of ratings for a list of movies by a user (return Map<String, Object>)
     * 
     * - addRating(long id_movie, long id_user, float rating): to add, modify or delete a rating by a user (return void)
     */
    private static final String BASE_URL = "http://localhost:8080"; // Base URL
    private static final HttpClient client = HttpClient.newHttpClient();


    public static float getRating(long id_movie, long id_user) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/ratings/get/" + id_movie + "/" + id_user))
                .GET() 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject jsonResponse = new JSONObject(response.body());
        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }
        return (float) jsonResponse.getLong("rating");
    }

    public static float averageRating(long id_movie) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/ratings/average/" + id_movie))
                .GET() 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        

        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }
        return Float.parseFloat(response.body());
    }

    public void addRating(long id_movie, long id_user, float rating) throws Exception {
        /*
         * This method is used to add, modify or delete a rating by a user.
         * If the user has already rated the movie, it modifies the rating.
         * If the user has not rated the movie, it adds a new rating.
         * If the old and new rate are equal, it deletes the rating.
         */
        String url = String.format("%s/ratings/get/%d/%d", BASE_URL, id_movie, id_user);

        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> responseGet = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        int status = responseGet.statusCode();

        if (status == 200) {
            JSONObject jsonResponse = new JSONObject(responseGet.body());
            float oldRating = (float) jsonResponse.getDouble("rating");

            if (oldRating != rating) {
                // Modifier la note
                String form = "id_movie=" + id_movie + "&id_user=" + id_user + "&rating=" + rating;
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/ratings/modify"))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .PUT(HttpRequest.BodyPublishers.ofString(form))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    throw new IOException("Modification échouée, code: " + response.statusCode());
                } else if (response.statusCode() == 401) {
                    System.out.println("Note supprimée : " + oldRating + " pour le film " + id_movie + " par l'utilisateur " + id_user);
                } else {
                    System.out.println("Note modifiée : ancienne = " + oldRating + ", nouvelle = " + rating);
                }
            }
        } else if (status == 401) {
            // Ajouter la note
            String form = "id_movie=" + id_movie + "&id_user=" + id_user + "&rating=" + rating;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/ratings/add"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 201 && response.statusCode() != 200) {
                throw new IOException("Ajout échoué, code: " + response.statusCode());
            } else {
            System.out.println("Note ajoutée : " + rating + " pour le film " + id_movie + " par l'utilisateur " + id_user);
        }
            } else {
                throw new IOException("Code retour inattendu: " + status);
            }
        }

        public static int nbRatings(long id_user) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/ratings/nbRatings/" + id_user))
                .GET() 
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Request failed with status: " + response.statusCode());
        }
            return Integer.parseInt(response.body());
            
        }
    }
