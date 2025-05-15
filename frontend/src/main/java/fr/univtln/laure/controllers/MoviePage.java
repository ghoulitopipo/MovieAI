package fr.univtln.laure.controllers;

import java.io.IOException;
import java.net.URL;

import fr.univtln.laure.utils.ApiRatings;
import fr.univtln.laure.utils.SceneChanger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class MoviePage {

    private static final int MAX_STARS = 5;
    private static final double STAR_SIZE = 40;

    @Getter
    @Setter
    private static long idMovie;    

    private float rating;
    private final Image halfStar = new Image(getClass().getResource("/images/star/star-half.png").toExternalForm());
    private final Image fullStar = new Image(getClass().getResource("/images/star/star-full.png").toExternalForm());
    private final Image blankStar = new Image(getClass().getResource("/images/star/star-blank.png").toExternalForm());

    @FXML
    private Button deconnexionButton;

    public void initialize() {
        try {
            rating = ApiRatings.getRating(idMovie, Home.getIdConnexion());
        } catch (Exception e) {
            rating = -1.f;
        }
        
    }

    public void closeMovie(){
        try {
            MoviePage.setIdMovie(0);

            URL fxmlUrl = getClass().getResource("/views/home.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) deconnexionButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Home");
            SceneChanger.setWindow(stage, "home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleDisconnect(){
        try {
            Home.setIdConnexion(0);
            MoviePage.setIdMovie(0);

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
    
}