package fr.univtln.laure.controllers;

import java.io.IOException;

import fr.univtln.laure.utils.ApiUsers;
import fr.univtln.laure.utils.SceneChanger;

import java.net.URL;

import org.json.JSONObject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Login {
    @FXML
    private Button connexionButton;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;


    private final int MAX_CHARACTERS = 40;

    @FXML
    public void initialize() {
        connexionButton.setDisable(true);

        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > MAX_CHARACTERS) {
                emailField.setText(oldValue);
            }

            connexionButton.setDisable(newValue.trim().isEmpty());
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > MAX_CHARACTERS) {
                passwordField.setText(oldValue);
            }
        });

        emailField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs !");
            return;
        }
        new Thread(() -> {
            try {
                String response = ApiUsers.login(email, password);
                if (response != null) {

                    JSONObject json = new JSONObject(response);
                    int connexionId = json.getInt("id");

                    Platform.runLater(() -> changeToAccueilSceneWithId(connexionId));
                }
                else {
                    System.out.println("Échec de connexion, vérifiez vos identifiants !");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erreur lors de la connexion !");
            }
        }).start();
    }

    @FXML
    private void handleLoginCA(){
        try {
            URL fxmlUrl = getClass().getResource("/views/createAccount.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Create Account");
            SceneChanger.setWindow(stage, "createAccount");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeToAccueilSceneWithId(int connexionId) {
        try {
            URL fxmlUrl = getClass().getResource("/views/home.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load());

            // Récupération du contrôleur et injection de l'ID
            Home controller = loader.getController();
            controller.setIdConnexion(connexionId);

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Home");
            SceneChanger.setWindow(stage, "home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}