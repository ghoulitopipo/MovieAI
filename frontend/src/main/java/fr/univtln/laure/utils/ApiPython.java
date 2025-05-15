package fr.univtln.laure.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ApiPython {

    public static String getRecommendationIndiGenreJson(int userId) {
        ProcessBuilder pb = new ProcessBuilder("python3", "recommendationindiGenre.py", String.valueOf(userId));
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line);
        }

        return output.toString();
    }
}
