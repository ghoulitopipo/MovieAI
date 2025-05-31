package fr.univtln.laure.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import fr.univtln.laure.utils.ApiMovies;
import fr.univtln.laure.utils.ApiPython;
import fr.univtln.laure.utils.ApiRatings;
import fr.univtln.laure.utils.ImgLoader;
import fr.univtln.laure.utils.MovieCache;
import fr.univtln.laure.utils.SceneChanger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Home {

    private static final Map<Long, Image> posterCache = new HashMap<>();

    private static JSONArray cachedMovieForYou = null;
    private static JSONArray cachedMovieByOthers = null;

    @FXML
    private Button disconectButton;

    @FXML
    private HBox recommendedContainer;
    @FXML
    private HBox likedContainer;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> searchSuggestions;

    private static long IdConnexion;

    public static long getIdConnexion() {
        return IdConnexion;
    }

    public static void setIdConnexion(long idConnexion) {
        IdConnexion = idConnexion;
    }

    private int scrollPaneElement = 0;
    private int otherPaneElement = 0;

    private JSONArray listMovieForYou;
    private JSONArray listMovieByOthers;

    private static class MoviePageData {
        List<Long> ids = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<String> genres = new ArrayList<>();
        List<Long> tmdbIds = new ArrayList<>();
        List<String> urls = new ArrayList<>();
    }

    private final MoviePageData recommendedData = new MoviePageData();
    private final MoviePageData likedData = new MoviePageData();

    @FXML
    public void initialize() {
        try {
            if (ApiRatings.nbRatings(IdConnexion) == 0) {
                listMovieForYou = ApiPython.RecommendationForNoData();
                listMovieByOthers = ApiPython.RecommendationForNoData();
            }
            else {
                listMovieForYou = ApiPython.RecommendationForYou(IdConnexion);
                listMovieByOthers = ApiPython.RecommendationForOther(IdConnexion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < recommendedContainer.getChildren().size(); i++) {
            int index = i;
            if (recommendedContainer.getChildren().get(i) instanceof StackPane) {
                StackPane stack = (StackPane) recommendedContainer.getChildren().get(i);
                stack.setOnMouseClicked(e -> openMovie(recommendedData, index));
            }
        }

        for (int i = 0; i < likedContainer.getChildren().size(); i++) {
            int index = i;
            if (likedContainer.getChildren().get(i) instanceof StackPane) {
                StackPane stack = (StackPane) likedContainer.getChildren().get(i);
                stack.setOnMouseClicked(e -> openMovie(likedData, index));
            }
        }

        if (searchSuggestions != null) {
            searchSuggestions.setVisible(false);
            searchSuggestions.setOnMouseClicked(event -> {
                String selected = searchSuggestions.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    searchField.setText(selected);
                    searchSuggestions.setVisible(false);
                    try {
                        JSONArray arr = ApiMovies.getMoviebyTitle(selected);
                        System.out.println("Selected movie: " + selected + " - JSONArray: " + arr);
                        if (arr.length() > 0) {
                            JSONObject obj = arr.getJSONObject(0);
                            long movieId = obj.getLong("id");
                            JSONArray movieArr = ApiMovies.getMoviebyID(movieId);
                            if (movieArr.length() > 0) {
                                JSONObject movieObj = movieArr.getJSONObject(0);
                                MoviePageData tempData = new MoviePageData();
                                tempData.ids.add(movieId);
                                tempData.titles.add(movieObj.getString("title"));
                                tempData.genres.add(movieObj.getString("genre"));
                                tempData.tmdbIds.add(movieObj.getLong("tmdb"));
                                tempData.urls.add(ImgLoader.getImg(movieObj.getLong("tmdb")));
                                openMovie(tempData, 0);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void showMovies(JSONArray movieArray, HBox container, int startIndex, MoviePageData data) {
        data.ids.clear();
        data.titles.clear();
        data.genres.clear();
        data.tmdbIds.clear();
        data.urls.clear();

        int end = Math.min(startIndex + 8, movieArray.length());

        for (int i = 0; i < container.getChildren().size(); i++) {
            if (container.getChildren().get(i) instanceof StackPane) {
                ((StackPane) container.getChildren().get(i)).getChildren().clear();
            }
        }

        for (int i = startIndex; i < end; i++) {
            try {
                JSONObject obj = movieArray.getJSONObject(i);
                long movieId = obj.getLong("movie_id");
                JSONArray movieArr = ApiMovies.getMoviebyID(movieId);
                if (movieArr.length() == 0) continue;
                JSONObject movieObj = movieArr.getJSONObject(0);

                String title = movieObj.getString("title");
                String genre = movieObj.getString("genre");
                long tmdbID = movieObj.getLong("tmdb");

                data.ids.add(movieId);
                data.titles.add(title);
                data.genres.add(genre);
                data.tmdbIds.add(tmdbID);

                String imgUrl = ImgLoader.getImg(tmdbID);
                Image image = posterCache.computeIfAbsent(tmdbID, id -> {
                    if (imgUrl.startsWith("/images/placeholder/")) {
                        return new Image(getClass().getResourceAsStream("/images/placeholder/placeholderPoster.jpg"));
                    } else {
                        return new Image(imgUrl);
                    }
                });
                data.urls.add(imgUrl);

                if (i - startIndex < container.getChildren().size()) {
                    StackPane stack = (StackPane) container.getChildren().get(i - startIndex);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(110);
                    imageView.setPreserveRatio(true);
                    stack.getChildren().add(imageView);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void updateMoviesN() {
        if (scrollPaneElement + 8 < listMovieForYou.length()) {
            scrollPaneElement += 8;
            showMovies(listMovieForYou, recommendedContainer, scrollPaneElement, recommendedData);
        }
    }

    @FXML
    private void updateMoviesB() {
        if (scrollPaneElement >= 8) {
            scrollPaneElement -= 8;
            showMovies(listMovieForYou, recommendedContainer, scrollPaneElement, recommendedData);
        }
    }

    @FXML
    private void updateMoviesON() {
        if (otherPaneElement + 8 < listMovieByOthers.length()) {
            otherPaneElement += 8;
            showMovies(listMovieByOthers, likedContainer, otherPaneElement, likedData);
        }
    }

    @FXML
    private void updateMoviesOB() {
        if (otherPaneElement >= 8) {
            otherPaneElement -= 8;
            showMovies(listMovieByOthers, likedContainer, otherPaneElement, likedData);
        }
    }

    private void openMovie(MoviePageData data, int stackPaneId) {
        if (stackPaneId >= data.ids.size()) return;
        try {
            long id = data.ids.get(stackPaneId);
            String title = data.titles.get(stackPaneId);
            Long tmdbID = data.tmdbIds.get(stackPaneId);
            String posterUrl = ImgLoader.getImg(tmdbID);
            String genre = data.genres.get(stackPaneId);

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
    public void handleDisconnect() {
        try {
            Home.setIdConnexion(0);
            clearCache();

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

    public static void clearCache() {
        System.out.println("Clearing cache...");
        cachedMovieForYou = null;
        cachedMovieByOthers = null;
    }

    @FXML
    public void searchName() {
        searchField.setVisible(true);

        searchField.setOnKeyReleased(event -> {
            String searchText = searchField.getText();
            if (searchText.isEmpty()) {
                if (searchSuggestions != null) searchSuggestions.setVisible(false);
                return;
            }
            try {
                JSONArray movieArr = ApiMovies.getMoviebyTitle(searchText);
                ObservableList<String> suggestions = FXCollections.observableArrayList();
                for (int i = 0; i < movieArr.length(); i++) {
                    String title = movieArr.getJSONObject(i).getString("title");
                    suggestions.add(title);
                }
                if (searchSuggestions != null) {
                    searchSuggestions.setItems(suggestions);
                    searchSuggestions.setVisible(!suggestions.isEmpty());
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (searchSuggestions != null) searchSuggestions.setVisible(false);
            }
        });
    }
}