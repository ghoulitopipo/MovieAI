package fr.univtln.laure.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import fr.univtln.laure.utils.ApiMovies;
import fr.univtln.laure.utils.SceneChanger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import org.json.JSONArray;

public class Home{
    @FXML
    private Button disconectButton;

    @FXML
    private HBox recommendedContainer;

    @Getter
    @Setter
    private static int IdConnexion;

    private int scrollPaneElement = 0; 

    @FXML
    public void handleDisconnect(){
        try {
            Home.setIdConnexion(0);

            URL fxmlUrl = getClass().getResource("/views/login.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load()); 
            Stage stage = (Stage) disconectButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            SceneChanger.setWindow(stage, "login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateMoviesN() {
        try {
            List<String> listMovie = ApiMovies.get8movies(scrollPaneElement);
            for (int i = 0; i < recommendedContainer.getChildren().size(); i++) {
                if (recommendedContainer.getChildren().get(i) instanceof StackPane) {
                    StackPane stack = (StackPane) recommendedContainer.getChildren().get(i);
                    stack.getChildren().clear();
                    Label label = new Label(listMovie.get(i));
                    label.setWrapText(true);
                    label.setMaxWidth(110); // un peu moins que le StackPane pour la marge
                    stack.getChildren().add(label);
                }
            }
            scrollPaneElement+=8;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateMoviesB() {
        if (scrollPaneElement == 0) {return;}
            else {
                scrollPaneElement -= 8;
            }

            try {
                List<String> listMovie = ApiMovies.get8movies(scrollPaneElement);
                for (int i = 0; i < recommendedContainer.getChildren().size(); i++) {
                    if (recommendedContainer.getChildren().get(i) instanceof StackPane) {
                        StackPane stack = (StackPane) recommendedContainer.getChildren().get(i);
                        stack.getChildren().clear();
                        Label label = new Label(listMovie.get(i));
                        label.setWrapText(true);
                        label.setMaxWidth(110); // un peu moins que le StackPane pour la marge
                        stack.getChildren().add(label);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public void openMovie(){
        try {
            MoviePage.setIdMovie(10);

            URL fxmlUrl = getClass().getResource("/views/moviePage.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) disconectButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("MoviePage");
            SceneChanger.setWindow(stage, "moviePage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
