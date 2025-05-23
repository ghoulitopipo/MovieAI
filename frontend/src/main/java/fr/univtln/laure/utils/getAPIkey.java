package fr.univtln.laure.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class getAPIkey {

    public static String loadApiKey() {
        Properties props = new Properties();
        String apiKey = null;

        try {
            // On récupère le répertoire de base du projet
            String baseDir = System.getProperty("user.dir");

            Path configPath = Paths.get(baseDir).resolve("../config.properties");

            // Charger le fichier
            FileInputStream fis = new FileInputStream(configPath.toFile());
            props.load(fis);
            fis.close();

            apiKey = props.getProperty("tmdb.api_key");

            if (apiKey == null) {
                System.err.println("Clé 'tmdb.api_key' non trouvée dans le fichier.");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de config.pro : " + e.getMessage());
        }

        return apiKey;
    }
}
