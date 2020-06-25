package com.example.Client.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class PageController implements Initializable {
    protected static final String EMPTY_STRING = "";
    protected RestTemplate restTemplate;
    protected static String session;
    protected static String username;
    protected static final String apiAddress = "http://localhost:8080/";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        restTemplate = new RestTemplate();
    }

    public void loadScene(Stage stageTheEventSourceNodeBelongs, String file) throws IOException {
        FXMLLoader newPageLoader = new FXMLLoader(getClass().getClassLoader().getResource(file));
        Parent newPane = newPageLoader.load();
        Scene newScene = new Scene(newPane, 800, 600);
        stageTheEventSourceNodeBelongs.setScene(newScene);
        stageTheEventSourceNodeBelongs.centerOnScreen();
        stageTheEventSourceNodeBelongs.show();
    }
}
