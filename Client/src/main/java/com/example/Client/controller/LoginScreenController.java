package com.example.Client.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class LoginScreenController extends PageController {


    @FXML
    public Button loginButton;
    @FXML
    public Button signUpButton;
    @FXML
    public TextField usernameField;
    @FXML
    public TextField passwordField;
    @FXML
    public Label infoLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        restTemplate = new RestTemplate();

    }
    @FXML
    public void clickLoginButton(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(username.equals(EMPTY_STRING) || password.equals(EMPTY_STRING))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Enter your username and password !");
            alert.showAndWait();
            usernameField.clear();
            passwordField.clear();
        }
        else {
            String jsonString = new JSONObject()
                    .put("username", username)
                    .put("password", password).toString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);
            ResponseEntity<String> response;
            try {
                response = restTemplate.exchange(apiAddress + "/user/login", HttpMethod.POST, entity, String.class);
                PageController.session = response.getBody();
                PageController.username = username;
                infoLabel.setText("You're logged in successfully!");
                //New Scene
                Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                stageTheEventSourceNodeBelongs.setTitle("Main");
                super.loadScene(stageTheEventSourceNodeBelongs,"main.fxml");
            } catch (HttpClientErrorException e) {
                infoLabel.setText("");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Wrong username or password !");
                alert.showAndWait();

                usernameField.clear();
                passwordField.clear();

            }
            catch (ResourceAccessException e)
            {
                infoLabel.setText("");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Connection Problem Try again!");
                alert.showAndWait();

                usernameField.clear();
                passwordField.clear();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

            //New Scene
    }

    @FXML
    public void clickSignUpButton() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Sign Up Dialog");
        dialog.setHeaderText("Registration for New User");

        // Set the button types.
        ButtonType loginButtonType = ButtonType.OK;
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> loginButton.setDisable(newValue.trim().isEmpty()));

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(username::requestFocus);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(usernamePassword -> {
            if (!usernamePassword.getKey().equals(EMPTY_STRING) &&  !usernamePassword.getValue().equals(EMPTY_STRING)) {
                    String jsonString = new JSONObject()
                            .put("username", usernamePassword.getKey())
                            .put("password", usernamePassword.getValue()).toString();

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);
                    try {
                        restTemplate.exchange(apiAddress + "/user/signup", HttpMethod.POST, entity, String.class);
                        infoLabel.setText("New User is created!");
                    }
                    catch (ResourceAccessException e)
                    {
                        infoLabel.setText("");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Connection Problem Try again!");
                        alert.showAndWait();

                        usernameField.clear();
                        passwordField.clear();

                    }
                    catch (IllegalArgumentException e){
                        e.printStackTrace();
                    }
                    catch (HttpClientErrorException e) {
                        infoLabel.setText("");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Username has already taken \rTry Again!");
                        alert.showAndWait();
                    }
                }
            });
            usernameField.clear();
            passwordField.clear();
        }

    }
