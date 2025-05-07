package fr.univtln.laure.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiUsers {
    private static final String BASE_URL = "http://localhost:8080"; // Base URL
    private static final HttpClient client = HttpClient.newHttpClient();

    public static String login(String email, String password) throws Exception {

        String form = "email=" + email + "&password=" + password;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/users/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form)) 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            return null; 
        }
    }

    public static String create(String username, String email, String password) throws Exception {

        String form = "username=" + username + "&email=" + email + "&password=" + password;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/users/create"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form)) 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            return null; 
        }
    }
}
