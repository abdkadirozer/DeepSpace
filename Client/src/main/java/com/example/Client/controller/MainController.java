package com.example.Client.controller;

import com.example.game.levelsAndServices.GameService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class MainController extends PageController {

    @FXML public Button exitButton;
    @FXML public Button updateButton;
    @FXML public Button leaderButton;
    @FXML public Button playButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        restTemplate = new RestTemplate();

    }

    //Starts Single Player Game
    public void clickPlayButton(ActionEvent actionEvent) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        GameService gameService = new GameService(username,stageTheEventSourceNodeBelongs);
        gameService.startGame();
    }
    //Opens Leaderboard Table
    public void clickLeaderButton(ActionEvent actionEvent) throws IOException {

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        super.loadScene(stageTheEventSourceNodeBelongs,"leaderboard.fxml");
    }


    //Opens update user
    public void clickUpdateButton(ActionEvent actionEvent) throws IOException {

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        super.loadScene(stageTheEventSourceNodeBelongs,"updateUser.fxml");
    }

    //back to Login page
    public void clickExitButton(ActionEvent actionEvent) throws IOException {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        super.loadScene(stageTheEventSourceNodeBelongs, "loginScreen.fxml");
    }

}
