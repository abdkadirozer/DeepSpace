package com.example.game.levelsAndServices;

import com.example.Client.controller.PageController;
import com.example.game.GameEnums;
import com.example.game.levelsAndServices.multiplayer.MultiplayerLevel;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameService{
    private String username;
    private final List<AbstractLevel> levels;
    private AbstractLevel currentLevel;
    private GraphicsContext gc;
    private AnimationTimer gameLoop;
    private final GameState gameState;
    private final KeyCombination cheatKey = KeyCombination.keyCombination(GameEnums.CHEAT_CODE);
    private Stage stage;

    public GameService(String username, Stage stage){
        this.username= username;
        this.stage = stage;
        this.levels = new ArrayList<>();
        this.levels.add(new Level1());
        this.levels.add(new Level2());
        this.levels.add(new Level3());
        this.levels.add(new Level4());
        this.levels.add(new MultiplayerLevel());
        this.gameState = new GameState();
        gameState.setUsername(username);
    }
    public void startGame(){
        stage.setTitle("Deep Space -"+ username +" is playing");

        Group root = new Group();
        Scene scene = new Scene(root, GameEnums.SCREEN_WIDTH, GameEnums.SCREEN_HEIGHT);
        Canvas canvas = new Canvas( GameEnums.SCREEN_WIDTH, GameEnums.SCREEN_HEIGHT);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
        //Cheat Handler
        scene.addEventHandler(KeyEvent.KEY_PRESSED,
                (key) -> {
                    if( cheatKey.match(key) && !currentLevel.getClass().equals(MultiplayerLevel.class)) {
                        currentLevel.isPassed = true;
                    }
                });
        // Set the current level and necessary variables.
        gameState.setCurrentLevel(1);
        gameState.setHealth(20);
        currentLevel = levels.get(0);
        levels.remove(0);
        currentLevel.isPassed = false;
        currentLevel.isOver = false;
        currentLevel.initialize(gameState);
        canvas.setOnMouseMoved(currentLevel.myMouseMoveEventHandler);

        gameLoop = new MyTimer(canvas);
        gameLoop.start();
    }
    private class MyTimer extends AnimationTimer{
        Canvas canvas;
        MyTimer(Canvas canvas) {
            this.canvas = canvas;
        }

        @Override
        public void handle(long l) {
            double time = (l-gameState.getLastTime()) / 1000000000;
            gameState.setTime(time);
            // IF level is successfully passed by the user.
            if(currentLevel.isPassed){
                //IF there are still levels left in the game.
                if (!levels.isEmpty()) {
                    currentLevel = levels.get(0);
                    levels.remove(0);
                    currentLevel.createSpaceShip(gameState);
                    gameState.restartCounter();
                    canvas.setOnMouseMoved(currentLevel.myMouseMoveEventHandler);
                    gameState.setCurrentLevel(gameState.getCurrentLevel() + 1); // Increment the current level which is showed on the top of the scene.
                }
                // IF we are here -> it means that all levels have been passed successfully, i.e., game is won.
                else {
                        System.out.println("CONGRATULATIONS!!!!");
                        gameLoop.stop();
                        gc.drawImage(GameEnums.CAT_WIN_IMAGE, 0,0, GameEnums.SCREEN_WIDTH, GameEnums.SCREEN_HEIGHT);
                        gc.setStroke(Color.RED);
                    if(!gameState.getOtherName().equals(""))
                        gc.strokeText(String.format("Your Score:  %d \n %s Score:  %d",gameState.getScore(),gameState.getOtherName(),gameState.getOtherscore()),380,400);
                    else
                        gc.strokeText(String.format("Your Score:  %d",gameState.getScore()),400,400);
                        pauseAfterFinished(5);
                }
            }
            // IF spaceship is dead, i.e., game is over.
            else if(currentLevel.isOver){
                System.out.println("GAME OVER");
                gameLoop.stop();
                //Draw the game over image.
                gc.drawImage(GameEnums.GAME_OVER_IMAGE, 0,0, GameEnums.SCREEN_WIDTH, GameEnums.SCREEN_HEIGHT);
                gc.setStroke(Color.RED);
                if(!gameState.getOtherName().equals(""))
                    gc.strokeText(String.format("Your Score:  %d \n %s Score:  %d",gameState.getScore(),gameState.getOtherName(),gameState.getOtherscore()),380,400);
                else
                    gc.strokeText(String.format("Your Score:  %d",gameState.getScore()),400,400);
                pauseAfterFinished(5);
            }
            else if(time >= 30/1000.0){
                gameState.setLastTime(l);
                currentLevel.gameLoop(gameState,gc);
            }
            gameState.incrementCycle();

        }

    }

    /**
     * This method will be called after a user either wins or loses the game.
     * @param secs
     */
    private void pauseAfterFinished (int secs) {
        PageController pageController = new PageController();
        PauseTransition pause = new PauseTransition(Duration.seconds(secs));
        pause.setOnFinished(event -> {
                saveScore();
                try {
                    // Load the Leaderboard scene.
                    pageController.loadScene(this.stage, "leaderboard.fxml");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
        });
        pause.play();
    }

    /**
     * We send the score of the user to the server using this method.
     */
    private void saveScore(){
            String jsonString = new JSONObject()
                    .put("username", username)
                    .put("point", gameState.getScore()).toString();
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);
            try {
                restTemplate.exchange(GameEnums.API_ADDRESS + "/score/add", HttpMethod.POST, entity, String.class);
            }
            catch (HttpClientErrorException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Operation failed !");
                alert.showAndWait();
            }
            catch (ResourceAccessException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Connection Problem !");
                alert.showAndWait();
            }
    }

}
