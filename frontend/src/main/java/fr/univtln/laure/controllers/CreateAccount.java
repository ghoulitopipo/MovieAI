package fr.univtln.laure.controllers;

import java.io.IOException;
import java.net.URL;

import fr.univtln.laure.utils.ApiUsers;
import fr.univtln.laure.utils.SceneChanger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class CreateAccount {
    
    @FXML
    private Button createButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private final int MAX_CHARACTERS = 40;

    @FXML
    public void initialize() {
        createButton.setDisable(true);

        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > MAX_CHARACTERS) {
                emailField.setText(oldValue);
            }

            createButton.setDisable(newValue.trim().isEmpty());
        });

        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > MAX_CHARACTERS) {
                usernameField.setText(oldValue);
            }

            createButton.setDisable(newValue.trim().isEmpty());
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
                createAccount();
            }
        });
    }

    @FXML
    private void createAccount(){
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs !");
            return;
        }
        new Thread(() -> {
            try {
                String response = ApiUsers.create(username, email, password);
                if (response != null) {
                    Platform.runLater(() -> {
                        loginChange();
                    });
                }
                else {
                    System.out.println("Identifiants déja pris?");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void loginChange(){
        try {
            URL fxmlUrl = getClass().getResource("/views/login.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            SceneChanger.setWindow(stage, "login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
