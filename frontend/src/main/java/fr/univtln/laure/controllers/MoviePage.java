package fr.univtln.laure.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URL;

import org.controlsfx.control.Rating;
import org.json.JSONArray;
import org.json.JSONObject;

import fr.univtln.laure.utils.SceneChanger;
import fr.univtln.laure.utils.ApiTags;
import fr.univtln.laure.utils.ApiMovies;

public class MoviePage {
    @FXML private ImageView posterImageView;
    @FXML private Label titleLabel;
    @FXML private Label genresLabel;
    @FXML private FlowPane tagsBar;
    @FXML private Rating rating;
    @FXML private Button backButton;
    @FXML private Button discoButton;
    @FXML private Label ratingLabel;

    private static long idMovie;    

    public static void setIdMovie(long id) {
    idMovie = id;
    }

    public static long getIdMovie() {
    return idMovie;
    }

    @FXML
    public void initialize() {
        loadMovie(idMovie);
        loadtags(idMovie);
        rating.setRating(0);
        rating.setPartialRating(true);
        rating.setMax(5);

        rating.ratingProperty().addListener((obs, oldVal, newVal) -> {
            double rounded = Math.round(newVal.doubleValue() * 2) / 2.0;
            rating.setRating(rounded);
        });
    }

    @FXML
    public void handleDisconnect(){
        try {
            Home.setIdConnexion(0);
            MoviePage.setIdMovie(0);
            URL fxmlUrl = getClass().getResource("/views/login.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load()); 
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            SceneChanger.setWindow(stage, "login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTagsBar(ObservableList<String> tags) {
    tagsBar.getChildren().clear();
    for (String tag : tags) {
        HBox tagBox = new HBox(4);
        tagBox.getStyleClass().add("tag-item");
        Label tagLabel = new Label(tag);
        tagLabel.getStyleClass().add("tag-label");
        tagLabel.setWrapText(true);

        Button closeBtn = new Button("✖");
        closeBtn.getStyleClass().add("tag-close-btn");
        closeBtn.setOnAction(e -> {
            tags.remove(tag);
            updateTagsBar(tags);
        });

        tagBox.getChildren().addAll(tagLabel, closeBtn);
        tagsBar.getChildren().add(tagBox);
    }
}

    private void loadMovie(long idMovie) {
    try {
        JSONArray result = ApiMovies.getMoviebyID(idMovie);

        if (result.length() > 0) {
    JSONObject movie = result.getJSONObject(0);

    System.out.println(movie.toString(2));

    String title = movie.optString("title", "Titre inconnu");
    titleLabel.setText(title);

    String genresRaw = movie.optString("genres", movie.optString("genre", ""));
    String genres = genresRaw.replace("|", ", ");
    genresLabel.setText(genres);
}
    } catch (Exception e) {
        e.printStackTrace();
        titleLabel.setText("Erreur de chargement");
        genresLabel.setText("");
    }
}

private void loadtags(long idMovie) {
    ObservableList<String> tags = FXCollections.observableArrayList();
    try {
        JSONArray result = ApiTags.getAll(idMovie);
        if (result.length() > 0) {
            for (int i = 0; i < result.length(); i++) {
                JSONObject tag = result.getJSONObject(i);
                String tagName = tag.optString("tag", "Tag inconnu");
                tags.add(tagName);
            }
        }
        updateTagsBar(tags);
    } catch (Exception e) {
        e.printStackTrace();
        titleLabel.setText("Erreur de chargement");
        genresLabel.setText("");
    }
}

    @FXML
    public void handleHome(){
        try {

            URL fxmlUrl = getClass().getResource("/views/home.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) discoButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Home");
            SceneChanger.setWindow(stage, "home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
