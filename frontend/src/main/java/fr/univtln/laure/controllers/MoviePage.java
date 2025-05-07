package fr.univtln.laure.controllers;

import java.io.IOException;
import java.net.URL;

import fr.univtln.laure.utils.SceneChanger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MoviePage {

    @FXML
    private Button deconnexionButton;

    public void handleDisconnect(){
        try {
            URL fxmlUrl = getClass().getResource("/views/login.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load());

            Home.setIdConnexion(0);

            Stage stage = (Stage) deconnexionButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            SceneChanger.setWindow(stage, "login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}