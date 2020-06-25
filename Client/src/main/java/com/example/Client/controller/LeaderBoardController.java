package com.example.Client.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class LeaderBoardController extends PageController {
    @FXML public TableView leaderBoard;
    @FXML public TableColumn<PseudoScore,String> usernameColumn;
    @FXML public TableColumn<PseudoScore,String > scoreColumn;
    @FXML public TableColumn<PseudoScore,String > timeColumn;
    @FXML public GridPane leaderboardTab;
    @FXML public Button getThisMonth;
    @FXML public  Button getThisWeek;
    @FXML public Button getAll;
    @FXML public Button backButton;
    @FXML public Button exitButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("point"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    }


    public static class PseudoScore{
        private final String username;
        private final String point;
        private final String date;

        PseudoScore(String username, String point, String date) {
            this.username = username;
            this.point = point;
            this.date = date;
        }
        public String getDate() {
            return date;
        }
        public String getUsername() {
            return username;
        }

        public String getPoint() {
            return point;
        }
    }
    public void clickLeaderButton() {
        ResponseEntity<List<Map<String,String>>> response = restTemplate.exchange(apiAddress + "/score/leaderboard/all_time", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {});
        setTable(Objects.requireNonNull(response.getBody()));
    }
    public void clickThisMonthButton() {
        ResponseEntity<List<Map<String,String>>> response = restTemplate.exchange(apiAddress + "/score/leaderboard/month", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {});
        setTable(Objects.requireNonNull(response.getBody()));
    }
    public void clickThisWeekButton() {
        ResponseEntity<List<Map<String,String>>> response = restTemplate.exchange(apiAddress + "/score/leaderboard/week", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {});
        setTable(Objects.requireNonNull(response.getBody()));
    }
    public void setTable(List<Map<String,String>> list)
    {
        leaderBoard.getItems().clear();

        List<PseudoScore> rows = list.stream().map(i -> new PseudoScore(i.get("username"), i.get("point"), i.get("date"))).collect(Collectors.toList());
        leaderBoard.setItems(FXCollections.observableList(rows));
    }
    public void clickBackButton(ActionEvent actionEvent) throws IOException {

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        super.loadScene(stageTheEventSourceNodeBelongs,"main.fxml");
    }

    public void clickExitButton(ActionEvent actionEvent) throws IOException {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        super.loadScene(stageTheEventSourceNodeBelongs, "loginScreen.fxml");
    }
}
