package fr.univtln.laure;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import fr.univtln.laure.utils.ApiMovies;
import fr.univtln.laure.utils.SceneChanger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
        Scene scene = new Scene(loader.load());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        SceneChanger.setWindow(primaryStage, "login");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {

        System.out.println(ApiMovies.ListNotRate(1, "Drama"));
        launch(args);
    }
}
