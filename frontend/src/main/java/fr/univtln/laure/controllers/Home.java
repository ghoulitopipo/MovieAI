package fr.univtln.laure.controllers;

import java.io.IOException;
import java.net.URL;

import fr.univtln.laure.utils.SceneChanger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class Home {

    @FXML
    private Button deconnexionButton;

    @Getter
    @Setter
    private static int idConnexion;

    public void handleDisconnect(){
        try {
            Home.setIdConnexion(0);

            URL fxmlUrl = getClass().getResource("/views/login.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load()); 
            Stage stage = (Stage) deconnexionButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            SceneChanger.setWindow(stage, "login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openMovie(){
        try {
            MoviePage.setIdMovie(10);

            URL fxmlUrl = getClass().getResource("/views/moviePage.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) deconnexionButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("MoviePage");
            SceneChanger.setWindow(stage, "moviePage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}