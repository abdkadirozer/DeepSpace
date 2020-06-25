package com.example.game.gameServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ConnectionHandler extends Thread {
    private ServerSocket serverSocket;
    private List<GameInstance> games;
    private Queue<Client> waiting_clients;

    public static final String SERVER_IP = "localhost";
    public static final int SERVER_PORT = 8081;


    public ConnectionHandler() throws IOException {

        serverSocket = new ServerSocket(SERVER_PORT);
        games = new ArrayList<>();
        waiting_clients = new LinkedList<>();

    }

    @Override
    public void run() {
        try {
            super.run();
            while (true) {
		System.out.println("Waiting for players");
                Socket socket = serverSocket.accept();
                Client client = new Client(socket);
                waiting_clients.add(client);
                System.out.println("Player joined");
                if (waiting_clients.size() >= 2) {
                    System.out.println("New Match !");
                    Client client1 = waiting_clients.poll();
                    Client client2 = waiting_clients.poll();
                    GameInstance newGame = new GameInstance(client1, client2);
                    games.add(newGame);
                    newGame.start();

                    Iterator<GameInstance> iter = games.iterator();
                    while (iter.hasNext()) {
                        GameInstance game = iter.next();
                        if (game.isOver())
                            iter.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
