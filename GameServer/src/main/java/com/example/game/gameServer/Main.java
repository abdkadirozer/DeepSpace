package com.example.game.gameServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        Thread server = new ConnectionHandler();
        server.start();
        server.join();
    }
}
