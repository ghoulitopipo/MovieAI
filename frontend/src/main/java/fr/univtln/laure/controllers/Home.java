package fr.univtln.laure.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import fr.univtln.laure.utils.ApiMovies;
import fr.univtln.laure.utils.ImgLoader;
import fr.univtln.laure.utils.MovieCache;
import fr.univtln.laure.utils.SceneChanger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

public class Home{

    private static final Map<Long, Image> posterCache = new HashMap<>();

    @FXML
    private Button disconectButton;

    @FXML
    private HBox recommendedContainer;

    @FXML
    private TextField searchField;

    private static int IdConnexion;

    public static int getIdConnexion() {
        return IdConnexion;
    }

    public static void setIdConnexion(int idConnexion) {
        IdConnexion = idConnexion;
    }

    private int scrollPaneElement = 0; 

    private List<Long> listIDMovie;
    private List<String> listTitles; 
    private List<String> listURL;
    private List<String> listGenres;
    private List<Long> listIMDB;

    @FXML
    public void initialize() {

                
            //ApiPython.RecommendationForYou(IdConnexion)
            //ApiPython.RecommendationForOther(IdConnexion)
            
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
            listURL = new ArrayList<>();
            listTitles = new ArrayList<>();
            listGenres = new ArrayList<>();
            listIMDB = new ArrayList<>();
            JSONArray MovieArr = ApiMovies.get8movies(scrollPaneElement);
            for (int i = 0; i < MovieArr.length(); i++) {
                org.json.JSONObject movieObj = MovieArr.getJSONObject(i);
                long id = movieObj.getLong("id");
                String title = movieObj.getString("title");
                String genre = movieObj.getString("genre");
                long tmdbID = movieObj.getLong("tmdb");

                listIDMovie.add(id);
                listTitles.add(title);
                listGenres.add(genre);
                listIMDB.add(tmdbID);


                if (!posterCache.containsKey(tmdbID)) {
                    String posterUrl = ImgLoader.getImg(tmdbID);
                    Image posterImage = new Image(posterUrl, true);
                    posterCache.put(tmdbID, posterImage);
                    listURL.add(posterUrl);
                } else {
                    listURL.add("CACHED");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMovies() {
    try {
        getData(scrollPaneElement);

        for (int i = 0; i < recommendedContainer.getChildren().size(); i++) {
            if (recommendedContainer.getChildren().get(i) instanceof StackPane) {
                StackPane stack = (StackPane) recommendedContainer.getChildren().get(i);
                stack.getChildren().clear();

                long tmdbID = ApiMovies.get8movies(scrollPaneElement).getJSONObject(i).getLong("tmdb");

                Image image;
                if (posterCache.containsKey(tmdbID)) {
                    image = posterCache.get(tmdbID);
                } else {
                    String url = ImgLoader.getImg(tmdbID);
                    image = new Image(url, true);
                    posterCache.put(tmdbID, image);
                }

                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(110);
                imageView.setPreserveRatio(true);
                stack.getChildren().add(imageView);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

        @FXML
    private void updateMoviesN() {
        scrollPaneElement += 8;
        updateMovies();
        getData(scrollPaneElement+8);
        System.out.println("scrollPaneElement: " + scrollPaneElement);
    }

    @FXML
    private void updateMoviesB() {
        if (scrollPaneElement == 0) return;
        scrollPaneElement -= 8;
        updateMovies();
    }

    @FXML
public void openMovie(int StackPaneid){
    try {
        long id = listIDMovie.get(StackPaneid);
        String title = listTitles.get(StackPaneid);
        Long tmdbID = listIMDB.get(StackPaneid);
        String posterUrl = ImgLoader.getImg(tmdbID);
        System.out.println("Poster URL: " + posterUrl);
        String genre = listGenres.get(StackPaneid);

        MovieCache.setId(id);
        MovieCache.setTitle(title);
        MovieCache.setPosterUrl(posterUrl);
        MovieCache.setGenres(genre);

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

    @FXML
    public void searchName(){
        searchField.setVisible(true);

        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String searchText = searchField.getText();
                try {
                    JSONArray MovieArr = ApiMovies.getMoviebyTitle(searchText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
