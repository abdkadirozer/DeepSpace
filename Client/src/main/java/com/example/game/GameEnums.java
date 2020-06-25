package com.example.game;

import com.example.game.levelsAndServices.GameService;
import javafx.scene.image.Image;

import java.io.InputStream;

public class GameEnums {

    public static final String API_ADDRESS ="http://localhost:8083/";
    public static final String SERVER_IP = "localhost";
    public static final int SERVER_PORT = 8081;
    public static final String CHEAT_CODE = "CTRL+SHIFT+9";

    public static final String JSON_START_KEY = "start";
    public static final String JSON_PLAYER_NAME = "playername";
    public static final String JSON_HAS_WINNER_KEY = "haswinner";
    public static final String JSON_PLAYER_X = "playerx";
    public static final String JSON_PLAYER_Y = "playery";
    public static final String JSON_PLAYER_HP = "playerhp";
    public static final String JSON_BOSS_HP = "bosshp";
    public static final String JSON_PLAYER_SCORE = "playerscore";



    // For creating aliens in AbstractLevel class. -> Used in createAliens() function.
    public static final int EasyAlien = 0;
    public static final int MediumAlien = 1;
    public static final int HardAlien = 2;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    public static final int USER_BULLET_WIDTH = 20;
    public static final int USER_BULLET_HEIGHT = 30;
    public static final int ALIEN_BULLET_WIDTH = 6;
    public static final int ALIEN_BULLET_HEIGHT = 9;

    public static final int SPACE_SHIP_HEALTH = 20;
    public static final int SPACE_SHIP_DAMAGE = 10;
    public static final int SPACE_SHIP_HEIGHT = 100;
    public static final int SPACE_SHIP_WIDTH = 150;

    public static final int EASY_ALIEN_HEIGHT = 40;
    public static final int EASY_ALIEN_WIDTH = 50;
    public static final int MEDIUM_ALIEN_HEIGHT = 40;
    public static final int MEDIUM_ALIEN_WIDTH = 60;
    public static final int HARD_ALIEN_HEIGHT = 50;
    public static final int HARD_ALIEN_WIDTH = 75;
    public static final int BOSS_ALIEN_HEIGHT = 200;
    public static final int BOSS_ALIEN_WIDTH = 300;

    public static final double USER_BULLET_VELOCITY = -250;
    public static final double ALIEN_BULLET_VELOCITY = 100;


    public static final int EASY_ALIEN_REWARD = 10;
    public static final int MEDIUM_ALIEN_REWARD = 30;
    public static final int HARD_ALIEN_REWARD = 100;
    public static final int BOSS_ALIEN_REWARD = 1000;
    public static final int BOSS_ALIEN_HIT_REWARD = 250;
    public static final int EASY_ALIEN_HP = 3;
    public static final int MEDIUM_ALIEN_HP = 7;
    public static final int MEDIUM_ALIEN_DMG = 2;
    public static final int HARD_ALIEN_HP = 15;
    public static final int HARD_ALIEN_DMG = 3;
    public static final int BOSS_ALIEN_HP = 500;
    public static final int BOSS_ALIEN_DMG = 6;

    private static final String BOSS_PNG = "boss.png";
    private static final String USER_BULLET_PNG = "user_bullet.png";
    private static final String OTHER_BULLET_PNG = "other_bullet.png";
    private static final String ALIEN_BULLET_PNG = "alien_bullet.png";
    private static final String BACKGROUND_PNG = "background.png";
    private static final String SPACE_SHIP_PNG = "spaceship.png";
    private static final String OTHER_SHIP_PNG = "othership.png";

    private static final String EASY_ALIEN_PNG = "enemy-small.png";
    private static final String MEDIUM_ALIEN_PNG = "enemy-medium.png";
    private static final String HARD_ALIEN_PNG = "enemy-big.png";
    private static final String GAME_OVER_JPG = "game_over.jpg";
    private static final String CAT_WIN_JPG = "cat_win.jpg";

    public static final Image BOSS_IMG = createImage(BOSS_PNG);
    public static final Image USER_BULLET_IMG = createImage(USER_BULLET_PNG);
    public static final Image OTHER_BULLET_IMG = createImage(OTHER_BULLET_PNG);
    public static final Image BACK_GROUND_IMG = createImage(BACKGROUND_PNG);
    public static final Image SPACE_SHIP_IMG = createImage(SPACE_SHIP_PNG);
    public static final Image OTHER_SHIP_IMG = createImage(OTHER_SHIP_PNG);
    public static final Image EASY_ALIEN_IMG = createImage(EASY_ALIEN_PNG);
    public static final Image ALIEN_BULLET_IMAGE = createImage(ALIEN_BULLET_PNG);
    public static final Image MEDIUM_ALIEN_IMG = createImage(MEDIUM_ALIEN_PNG);
    public static final Image HARD_ALIEN_IMG = createImage(HARD_ALIEN_PNG);
    public static final Image GAME_OVER_IMAGE = createImage(GAME_OVER_JPG);
    public static final Image CAT_WIN_IMAGE = createImage(CAT_WIN_JPG);

    public static final String OUT_OF_CONTEXT_RIP = "Out_Of_Context_RIP.mp3";


    public static Image createImage(String filename){
        InputStream path = GameService.class.getResourceAsStream("/assets/" + filename);
        return new Image(path);
    }

}
