package fr.univtln.laure.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ApiPython {

    public static String getRecommendationIndiGenreJson(int userId) {
        ProcessBuilder pb = new ProcessBuilder("python3", "recommendationindiGenre.py", String.valueOf(userId));
        Process process = null;
        try {
            process = pb.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return output.toString();
    }
}
