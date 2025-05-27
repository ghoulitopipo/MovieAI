package fr.univtln.laure.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

import fr.univtln.laure.utils.getAPIkey;

public class ImgLoader {

    private static final String API_KEY = getAPIkey.loadApiKey();
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    public static String getImg(Long id) {
        try {
            // Construire l'URL de l'API
            String urlStr = BASE_URL + id + "?api_key=" + API_KEY;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder responseContent = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                responseContent.append(inputLine);
            }
            in.close();

            JSONObject json = new JSONObject(responseContent.toString());

            if (json.has("poster_path") && !json.isNull("poster_path")) {
                String posterPath = json.getString("poster_path");
                return IMAGE_BASE_URL + posterPath;
            }
            System.err.println("Erreur lors de la connexion à l'API TMDB : code  "+ " pour l'URL " + urlStr);
            return null;

        } catch (Exception e) {
            return "/images/placeholder/placeholderPoster.jpg";
            
        }
    }
}
