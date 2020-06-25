package com.example.game.levelsAndServices.multiplayer;

import com.example.game.GameEnums;
import com.example.game.levelsAndServices.AbstractLevel;
import com.example.game.levelsAndServices.GameState;
import com.example.game.objects.BaseGameObject;
import com.example.game.objects.BossAlien;
import com.example.game.objects.SpaceShip;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class MultiplayerLevel extends AbstractLevel {


    private ClientHandler multiplayerHandler;
    //private int updatePeriod = 0;
    private SpaceShip otherShip;
    private int winner;
    protected int otherShootRate;
    private BossAlien bossAlien;
    protected ArrayList<BaseGameObject> otherBullets;

    public MultiplayerLevel() {
        super();
        generateBossAlien();
        winner = 0;
        otherShootRate = 10;
        otherBullets = new ArrayList<>();
    }

    public BossAlien getBossAlien() {
        return bossAlien;
    }

    public SpaceShip getOtherShip() {
        return otherShip;
    }

    public void setOtherShip(SpaceShip otherShip) {
        this.otherShip = otherShip;
    }


    @Override
    public void gameLoop(GameState gameState, GraphicsContext gc) {
        if (multiplayerHandler.isGameStarted()) {
            update(gameState);
            // IF collision happens -> health of spaceship and aliens will be updated.
            // IF spaceship or one of the aliens is dead -> will not be rendered. (setDead)
            detectCollisions(gameState);
            gameState.setOtherScore(otherShip.getScore());
            gameState.setScore(spaceShip.getScore());
            gameState.setOtherName(otherShip.getUsername());
            //Drawing spaceship,aliens,bullets and score & Health Text.
            gc.drawImage(GameEnums.BACK_GROUND_IMG, 0, 0, 800, 600);

            gc.setStroke(Color.WHITE);
            gc.strokeText(String.format("Current Score : %d", spaceShip.getScore()), 30, 30);
            gc.strokeText(String.format("Health : %d/%d", spaceShip.getHealthPoints(), GameEnums.SPACE_SHIP_HEALTH), 30, 50);
            gc.setStroke(Color.WHITE);
            gc.strokeText(String.format(otherShip.getUsername() + " Score : %d", otherShip.getScore()), 300, 30);
            gc.strokeText(String.format(otherShip.getUsername() + " Health : %d/%d", otherShip.getHealthPoints(), GameEnums.SPACE_SHIP_HEALTH), 300, 50);
            gc.strokeText(String.format("BOSS Health : %d/%d", bossAlien.getHealthPoints(), GameEnums.BOSS_ALIEN_HP), 450, 30);


            // Spaceship gets rendered here.
            if (!spaceShip.isDead())
                spaceShip.render(gc);
            // Spaceship shoots.
            userShoots();
            // Other spaceship shoots.
            otherShoots();
            // Aliens shoot.
            alienShoots(gameState);

            // Draw all objects in the game.
            if (!spaceShip.isDead())
                drawHelper(gc, userBullets);
            drawHelper(gc, alienBullets);
            bossAlien.render(gc);
            drawHelper(gc, otherBullets);
            otherShip.render(gc);
            try {
                multiplayerHandler.sendData();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            gc.drawImage(GameEnums.BACK_GROUND_IMG, 0, 0, GameEnums.SCREEN_WIDTH, GameEnums.SCREEN_HEIGHT);
            gc.setStroke(Color.WHITE);
            gc.strokeText("Waiting the other player...", 400, 300);
        }
    }

    @Override
    public void update(GameState gameState) {
        double time = gameState.getTime();
        // Spaceship is dead -> Game is over.
        if (spaceShip.isDead()) {
            isOver = true;
            this.setWinner(-1);
        }
        if (otherShip.isDead()) {
            isPassed = true;
            this.setWinner(1);
        }

        // IF there is no alien left in the current level.
        if (bossAlien.isDead()) {
            if (spaceShip.getScore() > otherShip.getScore()) {
                isPassed = true;
                this.setWinner(1);
            } else {
                isOver = true;
                this.setWinner(-1);
            }
        }
        // IF spaceship is NOT dead -> update spaceship and user bullets.
        if (!spaceShip.isDead()) {
            spaceShip.update(time);
            updateObjects(time, userBullets);
        }
        if (!otherShip.isDead()) {
            otherShip.update(time);
            updateObjects(time, otherBullets);
        }
        bossAlien.update(time);
        updateObjects(time, alienBullets);

    }


    @Override
    public void createSpaceShip(GameState gameState) {
        super.createSpaceShip(gameState);
        spaceShip.setUsername(gameState.getUsername());
        spaceShip.setScore(gameState.getScore());
        createOtherShip(gameState);
        System.out.println("MultiplayerLevel is Created !!");
        multiplayerHandler = new ClientHandler();
        multiplayerHandler.find(this);

    }

    /**
     * Creating the opponent ship, i.e., the ship controlled by the other user in the game.
     *
     * @param gameState
     */
    public void createOtherShip(GameState gameState) {

        otherShip = new SpaceShip(GameEnums.OTHER_SHIP_IMG, GameEnums.SPACE_SHIP_WIDTH, GameEnums.SPACE_SHIP_HEIGHT);
        otherShip.setHealthPoints(gameState.getHealth());
        otherShip.setDamage(GameEnums.SPACE_SHIP_DAMAGE);
        // Set initial position of the space ship.
        otherShip.setPos((GameEnums.SCREEN_WIDTH / 4), 500); // Might be changed later!!
    }

    /**
     * There will be only one alien in the final level -> boss alien.
     */
    public void generateBossAlien() {
        bossAlien = new BossAlien(GameEnums.BOSS_IMG, GameEnums.BOSS_ALIEN_WIDTH, GameEnums.BOSS_ALIEN_HEIGHT);
        bossAlien.setHealthPoints(GameEnums.BOSS_ALIEN_HP);
        bossAlien.setPos(GameEnums.SCREEN_WIDTH / 2, 100);
    }

    @Override
    public void alienShoots(GameState gameState) {
        // To change the frequency of the shooting. 40 is added to changeRate because of the interesting behavior of the cycle.
        long changeRate = gameState.getCycle() + 40;
        int min = 10;
        int max = 30;
        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
        //System.out.println(changeRate);
        if (changeRate % 10 == 0)
            alienShootRate--;
        if (alienShootRate == 0) {
            alienShootRate = randomNum;
            alienBullets.add(bossAlien.shoot());
        }
    }

    public void otherShoots() {
        otherShootRate--;
        if (otherShootRate == 0) {
            otherShootRate = 10;
            otherBullets.add(otherShip.shoot());
        }
    }

    /**
     * This method should be overrided because of the other spaceship added recently to the game.
     *
     * @param gameState
     */
    @Override
    public void detectCollisions(GameState gameState) {
        for (BaseGameObject userbullet : userBullets) {
            if (!userbullet.isDead())
                if (!bossAlien.isDead() && intersects(userbullet, bossAlien)) {
                    bossAlien.shotBy(userbullet, gameState);
                    spaceShip.updateScore(bossAlien.getReward() * GameEnums.SPACE_SHIP_DAMAGE / GameEnums.BOSS_ALIEN_HP);
                    if (bossAlien.isDead())
                        spaceShip.updateScore(GameEnums.BOSS_ALIEN_HIT_REWARD);
                }
        }

        // IF an alien bullet collides with the spaceship.
        for (BaseGameObject alienbullet : alienBullets) {
            if (!alienbullet.isDead() && intersects(alienbullet, spaceShip))
                spaceShip.shotBy(alienbullet, gameState);
        }

        // IF an other bullet collides with an alien.
        for (BaseGameObject otherbullet : otherBullets) {
            if (!otherbullet.isDead())
                if (!bossAlien.isDead() && intersects(otherbullet, bossAlien)) {
                    bossAlien.shotBy(otherbullet, gameState);
                    otherShip.updateScore(bossAlien.getReward() * GameEnums.SPACE_SHIP_DAMAGE / GameEnums.BOSS_ALIEN_HP);
                    if (bossAlien.isDead())
                        otherShip.updateScore(GameEnums.BOSS_ALIEN_HIT_REWARD);

                }
        }


        // IF an alien bullet collides with the other spaceship.
        for (BaseGameObject alienbullet : alienBullets) {
            if (!alienbullet.isDead() && intersects(alienbullet, otherShip))
                otherShip.shotBy(alienbullet, gameState);
        }
    }

    /**
     * Method to update other player's spaceship to handle synchronization issues during the gameplay.
     *
     * @param receivedData
     */
    public void updateOtherShip(JSONObject receivedData) {
        otherShip.setPosX(receivedData.getDouble(GameEnums.JSON_PLAYER_X));
        otherShip.setPosY(receivedData.getDouble(GameEnums.JSON_PLAYER_Y));
        otherShip.setUsername(receivedData.getString(GameEnums.JSON_PLAYER_NAME));
        if (otherShip.getScore() < receivedData.getInt(GameEnums.JSON_PLAYER_SCORE))
            otherShip.setScore(receivedData.getInt(GameEnums.JSON_PLAYER_SCORE));
        if (otherShip.getHealthPoints() > receivedData.getInt(GameEnums.JSON_PLAYER_HP))
            otherShip.setHealthPoints(receivedData.getInt(GameEnums.JSON_PLAYER_HP));
        if (bossAlien.getHealthPoints() > receivedData.getInt(GameEnums.JSON_BOSS_HP))
            bossAlien.setHealthPoints(receivedData.getInt(GameEnums.JSON_BOSS_HP));

    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }
}
