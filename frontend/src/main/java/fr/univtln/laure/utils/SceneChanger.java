package fr.univtln.laure.utils;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneChanger {

    public static void changeScene(Stage stage, String fxmlFile, String title) {
        // Utilisation de Platform.runLater pour s'assurer que le changement de scène se fait sur le thread principal
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(SceneChanger.class.getResource("/views/" + fxmlFile));
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.setTitle(title);
                setWindow(stage, title);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void setWindow(Stage stage, String fileName) {
        switch(fileName) {
            case "home":
                stage.setWidth(1100);
                stage.setHeight(650);
                stage.setX(360);
                stage.setY(130);
                break;
            case "moviePage":
                stage.setWidth(1340);
                stage.setHeight(800);
                stage.setX(360);
                stage.setY(130);
                break;
            case "login":
                stage.setWidth(1200);
                stage.setHeight(800);
                stage.setX(360);
                stage.setY(130);
                break;
            default:
                stage.setWidth(1200);
                stage.setHeight(800);
                stage.setX(460);
                stage.setY(180);
                break;
        }
    }
}