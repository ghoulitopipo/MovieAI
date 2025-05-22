package fr.univtln.laure.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import fr.univtln.laure.utils.ApiMovies;
import fr.univtln.laure.utils.ApiPython;
import fr.univtln.laure.utils.SceneChanger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Home{
    @FXML
    private Button disconectButton;

    @FXML
    private HBox recommendedContainer;

    private static int IdConnexion;

    public static int getIdConnexion() {
        return IdConnexion;
    }

    public static void setIdConnexion(int idConnexion) {
        IdConnexion = idConnexion;
    }

    private int scrollPaneElement = 0; 

    List<Long> listIDMovie;
    List<String> listNameMovie;

    @FXML
    public void initialize() {
        
            //ApiPython.RecommendationForYou(IdConnexion)
        
        for (int i = 0; i < recommendedContainer.getChildren().size(); i++) {
            int index = i; 
            if (recommendedContainer.getChildren().get(i) instanceof StackPane) {
                StackPane stack = (StackPane) recommendedContainer.getChildren().get(i);
                System.out.println("StackPane " + i);
                stack.setOnMouseClicked(e -> openMovie(index));
            }
        }
    }

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

    private void getData(int nb){
    try {
        listIDMovie = new ArrayList<>();
        listNameMovie = new ArrayList<>();
        JSONArray MovieArr = ApiMovies.get8movies(scrollPaneElement);
        for (int i = 0; i < MovieArr.length(); i++) {
            org.json.JSONObject movieObj = MovieArr.getJSONObject(i);
            long id = movieObj.getLong("id");
            listIDMovie.add(id);
            String title = movieObj.getString("title");
            listNameMovie.add(title);
        }
    } catch (Exception e) {
        e.printStackTrace();
        }
    }

    @FXML
    private void updateMoviesN() {
        try {
            getData(scrollPaneElement);
            for (int i = 0; i < recommendedContainer.getChildren().size(); i++) {
                if (recommendedContainer.getChildren().get(i) instanceof StackPane) {
                    StackPane stack = (StackPane) recommendedContainer.getChildren().get(i);
                    stack.getChildren().clear();
                    Label label = new Label(listNameMovie.get(i));
                    label.setWrapText(true);
                    label.setMaxWidth(110); 
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
                getData(scrollPaneElement);
                for (int i = 0; i < recommendedContainer.getChildren().size(); i++) {
                    if (recommendedContainer.getChildren().get(i) instanceof StackPane) {
                        StackPane stack = (StackPane) recommendedContainer.getChildren().get(i);
                        stack.getChildren().clear();
                        Label label = new Label(listNameMovie.get(i));
                        label.setWrapText(true);
                        label.setMaxWidth(110); 
                        stack.getChildren().add(label);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    @FXML
    public void openMovie(int StackPaneid){
        try {
            System.out.println("Movie ID: " + listIDMovie.get(StackPaneid));
            MoviePage.setIdMovie(listIDMovie.get(StackPaneid));

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
