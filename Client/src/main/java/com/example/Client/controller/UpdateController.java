package com.example.Client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateController extends PageController {

    @FXML public Button exitButton;

    @FXML public Button backButton;

    @FXML public TextField usernameField;
    @FXML public PasswordField passwordField;
    @FXML public Label infoLabel;
    @FXML public Button deleteButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }

    public void clickExitButton(ActionEvent actionEvent) throws IOException {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        super.loadScene(stageTheEventSourceNodeBelongs, "loginScreen.fxml");

    }

    public void clickBackButton(ActionEvent actionEvent) throws IOException {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        super.loadScene(stageTheEventSourceNodeBelongs,"main.fxml");
    }

    public void clickChangeButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(username.equals(EMPTY_STRING))
            username = null;
        if(password.equals(EMPTY_STRING))
            password = null;
        String jsonString = new JSONObject()
                    .put("username", username)
                    .put("password", password)
                    .put("session", session).toString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);
            try {
                restTemplate.exchange(apiAddress + "/user/update", HttpMethod.PUT, entity, String.class);
                infoLabel.setText("Profile is updated !");
                PageController.username = username;

            } catch (HttpClientErrorException e) {
                infoLabel.setText("Try different Username");
                usernameField.clear();
                passwordField.clear();
            }
            catch (ResourceAccessException e)
            {
                infoLabel.setText("Connection Problem Try again!");
                usernameField.clear();
                passwordField.clear();
            }
    }

    public void clickDeleteButton(ActionEvent actionEvent) {String jsonString = new JSONObject()
            .put("username", username)
            .put("session", session).toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);
        try {
            restTemplate.exchange(apiAddress + "/user/delete", HttpMethod.DELETE, entity, String.class);
            Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            super.loadScene(stageTheEventSourceNodeBelongs, "loginScreen.fxml");
        } catch (HttpClientErrorException | IOException e) {
            e.printStackTrace();
        }
        catch (ResourceAccessException e)
        {
            infoLabel.setText("Connection Problem !");
        }

    }
}
