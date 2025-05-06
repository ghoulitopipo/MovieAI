package fr.univtln.laure.controllers;

import java.io.IOException;


import fr.univtln.laure.utils.SceneChanger;

import java.net.URL;

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


    private final int maxCharacters = 40;

    @FXML
    public void initialize() {
        connexionButton.setDisable(true);

        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Limiter le nombre de caratères
            if (newValue.length() > maxCharacters) {
                emailField.setText(oldValue);
            }

            // Activer le bouton de connexion si les champs ne sont pas vides
            connexionButton.setDisable(newValue.trim().isEmpty());
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Limiter le nombre de caratères
            if (newValue.length() > maxCharacters) {
                passwordField.setText(oldValue);
            }
        });

        // Se connecter plus vite avec le bouton Entrée
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
        /*String email = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Vérifier si les champs ne sont pas vides
        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs !");
            return;
        }
        // Utiliser un thread pour effectuer l'appel réseau sans bloquer l'UI
        new Thread(() -> {
            try {
                // Appel au service API pour l'authentification
                String response = ApiUsers.login(email, password, this.getButtonInfo());
                if (response != null) {
                    System.out.println("Connexion réussie ! " + response);

                    JSONObject json = new JSONObject(response);
                    int connexionId = json.getInt("id");

                    Platform.runLater(() -> changeToAccueilSceneWithId(connexionId));
                }
                else {
                    // Si la connexion échoue, afficher un message d'erreur
                    System.out.println("Échec de connexion, vérifiez vos identifiants !");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erreur lors de la connexion !");
            }
        }).start();*/
        changeToAccueilSceneWithId(1);
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