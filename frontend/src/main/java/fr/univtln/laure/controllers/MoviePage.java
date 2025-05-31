package fr.univtln.laure.controllers;

import java.io.IOException;
import java.net.URL;

import org.controlsfx.control.Rating;
import org.json.JSONArray;
import org.json.JSONObject;

import fr.univtln.laure.utils.ApiRatings;
import fr.univtln.laure.utils.ApiTags;
import fr.univtln.laure.utils.MovieCache;
import fr.univtln.laure.utils.SceneChanger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MoviePage {
    @FXML private ImageView posterImageView;
    @FXML private Label titleLabel;
    @FXML private Label genresLabel;
    @FXML private FlowPane tagsBar;
    @FXML private Rating rating;
    @FXML private Button backButton;
    @FXML private Button discoButton;
    @FXML private Label ratingLabel;

    private long idMovie = MovieCache.getId();

    @FXML
    public void initialize() {
        loadMovie(idMovie);
        loadtags(idMovie);
        rating.setRating(0);
        rating.setPartialRating(true);
        rating.setMax(5);
        posterImageView.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                posterImageView.fitHeightProperty().bind(newScene.heightProperty());
            }
        });

        rating.ratingProperty().addListener((obs, oldVal, newVal) -> {
        double rounded = Math.round(newVal.doubleValue() * 2) / 2.0;
        if (rounded == 0) {
            return;
        }
        if (rounded != newVal.doubleValue()) {
            rating.setRating(rounded);
            return;
        }
        try {
            long userId = Home.getIdConnexion();
            ApiRatings apiRatings = new ApiRatings();
            apiRatings.addRating(idMovie, userId, (float) rounded);
            ratingLabel.setText("Votre note : " + rounded + " / 5");
        } catch (Exception e) {
            e.printStackTrace();
            rating.setRating(0);
            ratingLabel.setText("Note supprimée");
        }
    });}

    @FXML
    public void handleDisconnect(){
        try {
            Home.setIdConnexion(0);
            Home.clearCache();
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
        String posterUrl = MovieCache.getPosterUrl();
        String title = MovieCache.getTitle();
        String genresRaw = MovieCache.getGenres();
        String genres = genresRaw != null ? genresRaw.replace("|", ", ") : "";

        titleLabel.setText(title != null ? title : "");
        genresLabel.setText(genres);

        if (posterUrl != null && !posterUrl.isEmpty()) {
            Image image = new Image(posterUrl, true);
            posterImageView.setImage(image);
        } else {
            posterImageView.setImage(null);
        }
    }

    private void loadtags(long idMovie) {
        ObservableList<String> tags = FXCollections.observableArrayList();
        try {
            System.out.println("ID Movie: " + idMovie);
            JSONArray result = ApiTags.getAll(idMovie);
            if (result.length() > 0) {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject tag = result.getJSONObject(i);
                    String tagName = tag.optString("tag", "Tag inconnu");
                    tags.add(tagName);
                }
            }
            System.out.println(tags);
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
