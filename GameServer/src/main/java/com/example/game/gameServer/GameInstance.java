package com.example.game.gameServer;

import org.json.JSONObject;

import java.io.IOException;

public class GameInstance extends Thread {
    private final Client client1;
    private final Client client2;
    private boolean isOver;

    public static final String JSON_START_KEY = "start";
    public static final String JSON_HAS_WINNER_KEY = "haswinner";

    public GameInstance(Client c1, Client c2) {
        this.isOver = false;
        client1 = c1;
        client2 = c2;

    }

    @Override
    public void run() {
        new ClientListener(client1, 0).start();
        new ClientListener(client2, 1).start();


        // Server sends ACK message to the connected clients, and they start to send game data.
        JSONObject initial_message = new JSONObject().put(JSON_START_KEY, 1);
        send_message(initial_message, client1);
        send_message(initial_message, client2);
        while (true) {
            if (isOver) {
                client1.down();
                client2.down();
                break;
            }
        }
    }

    /**
     * Server sends message to clients synchronously.
     *
     * @param message
     * @param destination_client
     */
    private synchronized void send_message(JSONObject message, Client destination_client) {
        destination_client.out.println(message);
        destination_client.out.flush();
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    private class ClientListener extends Thread {
        private final Client client;
        private final int id;

        public ClientListener(Client client, int id) {
            this.client = client;
            this.id = id;
        }

        @Override
        public void run() {
            super.run();
            String received_message = "";
            while (!isOver) {
                try {
                    if ((received_message = client.in.readLine()).equals(null)) break;
                } catch (IOException e) {
                    isOver = true;
                }
                JSONObject message = new JSONObject(received_message);

                System.out.println("From "+id+" -> "+ message.toString());

                int winner_side = message.getInt(JSON_HAS_WINNER_KEY);

                if (winner_side != 0) {
                    JSONObject win_message = new JSONObject().put(JSON_HAS_WINNER_KEY,1);
                    JSONObject loose_message = new JSONObject().put(JSON_HAS_WINNER_KEY,-1);
                    if((id == 0 && winner_side==1) ||(id== 1 && winner_side == -1)) {
                        send_message(win_message,client1);
                        send_message(loose_message,client2);
                    }
                    else  //other client won
                    {
                        send_message(win_message, client2);
                        send_message(loose_message, client1);
                    }
                    isOver =true;
                    break;
                }
                else {
                    if (id == 0)
                        send_message(message, client2);
                    else
                        send_message(message, client1);
                }
            }
        }
    }
}
