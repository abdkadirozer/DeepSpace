package com.example.game.levelsAndServices.multiplayer;

import com.example.game.GameEnums;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler {
    private MultiplayerLevel multiplayerLevel;
    private PrintWriter out;
    private BufferedReader in;

    private boolean game_end_flag = false;
    private boolean game_start_flag = false;

    public boolean isGameEnded() {
        return game_end_flag;
    }

    public boolean isGameStarted() {
        return game_start_flag;
    }


    public void find(MultiplayerLevel multiplayerLevel) {
        this.multiplayerLevel = multiplayerLevel;

        try {
            Socket serverSocket = new Socket(GameEnums.SERVER_IP, GameEnums.SERVER_PORT);
            this.out = new PrintWriter(serverSocket.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread receiver = new Thread("receiver") {
            @Override
            public void run() {
                try {
                    receiveData();
                } catch (IOException e) {
                    game_end_flag = true;
                }
            }
        };
        receiver.start();
    }

    public void receiveData() throws IOException {
        while (game_end_flag == false) {
            JSONObject receivedData = new JSONObject(in.readLine());
            if (!receivedData.isNull(GameEnums.JSON_START_KEY)) // check if game has started
                game_start_flag = true;
            if (!receivedData.isNull(GameEnums.JSON_HAS_WINNER_KEY)) {
                if (receivedData.getInt(GameEnums.JSON_HAS_WINNER_KEY)== 0)
                    multiplayerLevel.updateOtherShip(receivedData);
                else {
                    game_end_flag = true;
                    if (receivedData.getInt(GameEnums.JSON_HAS_WINNER_KEY)== 1)
                        multiplayerLevel.setPassed(true);
                    else if (receivedData.getInt(GameEnums.JSON_HAS_WINNER_KEY)== -1) {
                        multiplayerLevel.setOver(true);
                        multiplayerLevel.getOtherShip().updateScore(GameEnums.BOSS_ALIEN_HIT_REWARD + 20);
                    }

                }
            }
        }

    }

    public void sendData() throws IOException {
        JSONObject sendData = new JSONObject();

        sendData.put(GameEnums.JSON_HAS_WINNER_KEY, multiplayerLevel.getWinner());

        if (multiplayerLevel.getWinner() == 0) {
            sendData.put(GameEnums.JSON_PLAYER_NAME,multiplayerLevel.getSpaceShip().getUsername());
            sendData.put(GameEnums.JSON_PLAYER_X, multiplayerLevel.getSpaceShip().getPosX());
            sendData.put(GameEnums.JSON_PLAYER_Y, multiplayerLevel.getSpaceShip().getPosY());
            sendData.put(GameEnums.JSON_PLAYER_HP, multiplayerLevel.getSpaceShip().getHealthPoints());
            sendData.put(GameEnums.JSON_BOSS_HP, multiplayerLevel.getBossAlien().getHealthPoints());
            sendData.put(GameEnums.JSON_PLAYER_SCORE,multiplayerLevel.getSpaceShip().getScore());
        }
        out.println(sendData.toString());
        out.flush();


    }


}
