package com.example.game.levelsAndServices;

import com.example.game.GameEnums;
import com.example.game.objects.*;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Abstract level class. It is created to implement game level classes in an easier and more concise way.
 * It contains desired functionality in game levelsAndServices and forms the basic infrastructure of the game.
 * By this way, implementing and extending our game features will be easily handled.
 */

public abstract class AbstractLevel {


    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    protected SpaceShip spaceShip;
    protected ArrayList<BaseGameObject> aliens;
    protected ArrayList<BaseGameObject> userBullets;
    protected ArrayList<BaseGameObject> alienBullets;
    protected boolean isPassed; // To indicate that level is completed successfully by the user.
    protected boolean isOver; // To indicate that user has failed to complete the current level.
    protected int userShootRate;
    protected int alienShootRate;
    protected MouseMoveEventHandler myMouseMoveEventHandler;

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }


    // Constructor
    public AbstractLevel() {
        isPassed = false;
        isOver = false;
        aliens = new ArrayList<>();
        userBullets = new ArrayList<>();
        alienBullets = new ArrayList<>();
        alienShootRate = 10;
        userShootRate = 10;
    }

    /**
     * Creating our spaceship for each level here in this method.
     *
     * @param gameState
     */
    public void createSpaceShip(GameState gameState) {
        myMouseMoveEventHandler = new MouseMoveEventHandler(this);
        spaceShip = new SpaceShip(GameEnums.SPACE_SHIP_IMG, GameEnums.SPACE_SHIP_WIDTH, GameEnums.SPACE_SHIP_HEIGHT);
        spaceShip.setHealthPoints(gameState.getHealth());
        spaceShip.setDamage(GameEnums.SPACE_SHIP_DAMAGE);
        // Set initial position of the space ship.
        spaceShip.setPos((GameEnums.SCREEN_WIDTH / 2), 500);
    }

    /**
     * Creating all aliens(Easy, Medium, Hard) for each level here in this method.
     *
     * @param alienType
     * @param alienInEachRow
     * @param rows
     * @param offsetX
     * @param gapX
     * @param offsetY
     * @param gapY
     * @return
     */
    public static LinkedList<BaseGameObject> createAliens(int alienType, int alienInEachRow, int rows, int offsetX, int gapX, int offsetY, int gapY) {
        LinkedList<BaseGameObject> newAliens = new LinkedList<>();
        int totalAliens = alienInEachRow * rows;
        for (int i = 0; i < totalAliens; i++) {
            BaseGameObject alien = null;

            switch (alienType) {
                case GameEnums.EasyAlien: {
                    alien = new EasyAlien(GameEnums.EASY_ALIEN_IMG, GameEnums.EASY_ALIEN_WIDTH, GameEnums.EASY_ALIEN_HEIGHT);
                    alien.setHealthPoints(GameEnums.EASY_ALIEN_HP);
                    break;
                }
                case GameEnums.MediumAlien: {
                    alien = new MediumAlien(GameEnums.MEDIUM_ALIEN_IMG, GameEnums.MEDIUM_ALIEN_WIDTH, GameEnums.MEDIUM_ALIEN_HEIGHT);
                    alien.setHealthPoints(GameEnums.MEDIUM_ALIEN_HP);
                    break;
                }
                case GameEnums.HardAlien: {
                    alien = new HardAlien(GameEnums.HARD_ALIEN_IMG, GameEnums.HARD_ALIEN_WIDTH, GameEnums.HARD_ALIEN_HEIGHT);
                    alien.setHealthPoints(GameEnums.HARD_ALIEN_HP);
                    break;
                }
            }
            assert alien != null;
            alien.setPos(offsetX + gapX * (i % alienInEachRow), offsetY + gapY * (i / alienInEachRow));
            newAliens.add(alien);

        }
        return newAliens;

    }

    public void userShoots() {
        userShootRate--;
        if (userShootRate == 0) {
            userShootRate = 10;
            userBullets.add(spaceShip.shoot());
        }
    }

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
            for (BaseGameObject alien : aliens) {   // To prevent casting problem of easy and medium aliens to a hard alien.
                if (alien.getDamage() == GameEnums.HARD_ALIEN_DMG)
                    alienBullets.add(((HardAlien) alien).shoot());
            }
        }


    }


    public void gameLoop(GameState gameState, GraphicsContext gc) {
        update(gameState);
        // IF collision happens -> health of spaceship and aliens will be updated.
        // IF spaceship or one of the aliens is dead -> will not be rendered. (setDead)
        detectCollisions(gameState);
        //Drawing spaceship,aliens,bullets and score & Health Text.
        gc.drawImage(GameEnums.BACK_GROUND_IMG, 0, 0, 800, 600);
        double scoreXOffset = 30;
        double healthStatusXOffset = 300;
        double levelXOffset = 500;
        gc.setStroke(Color.WHITE);
        gc.strokeText(String.format("Current Score : %d", gameState.getScore()), scoreXOffset, 30);
        gc.strokeText(String.format("Health : %d/%d", gameState.getHealth(), GameEnums.SPACE_SHIP_HEALTH), healthStatusXOffset, 30);
        gc.strokeText(String.format("Level : %d", gameState.getCurrentLevel()), levelXOffset, 30);

        // Spaceship gets rendered here.
        if (!spaceShip.isDead())
            spaceShip.render(gc);
        // Spaceship shoots.
        userShoots();
        // Aliens shoot.
        alienShoots(gameState);

        // Draw all objects in the game.
        if (!spaceShip.isDead())
            drawHelper(gc, userBullets);
        drawHelper(gc, alienBullets);
        drawHelper(gc, aliens);
    }

    /**
     * Helper method to draw objects other than the spaceship in the game.
     *
     * @param gc
     * @param objects
     */
    public void drawHelper(GraphicsContext gc, ArrayList<BaseGameObject> objects) {
        for (BaseGameObject object : objects) {
            if (!object.isDead())
                object.render(gc);
        }
    }

    protected void update(GameState gameState) {
        double time = gameState.getTime();
        // Spaceship is dead -> Game is over.
        if (spaceShip.isDead()) {
            isOver = true;
        }
        // IF there is no alien left in the current level.
        if (aliens.size() == 0)
            isPassed = true;

        // IF spaceship is NOT dead -> update spaceship and user bullets.
        if (!spaceShip.isDead()) {
            spaceShip.update(time);
            updateObjects(time, userBullets);
        }

        updateObjects(time, aliens);
        updateObjects(time, alienBullets);

    }

    /**
     * Update helper method for objects other than spaceship.
     *
     * @param time
     * @param objects
     */
    protected void updateObjects(double time, ArrayList<BaseGameObject> objects) {
        Iterator<BaseGameObject> iter = objects.iterator();

        while (iter.hasNext()) {
            BaseGameObject object = iter.next();
            // IF aliens quit from the bottom of the screen -> game is over.
            // Also, game shouldn't be over if an alien bullet does that.
            if (object.getWidth() != GameEnums.ALIEN_BULLET_WIDTH && object.getPosY() + object.getHeight() > GameEnums.SCREEN_HEIGHT)
                isOver = true;
            if (object.isDead())
                iter.remove();
            else
                object.update(time);
        }
    }

    /**
     * Helper intersects method for collision detection.
     *
     * @param obj1
     * @param obj2
     * @return
     */
    public boolean intersects(BaseGameObject obj1, BaseGameObject obj2) {
        Rectangle2D objRect1 = new Rectangle2D(obj1.getPosX(), obj1.getPosY(), obj1.getWidth(), obj1.getHeight());
        Rectangle2D objRect2 = new Rectangle2D(obj2.getPosX(), obj2.getPosY(), obj2.getWidth(), obj2.getHeight());
        // Return true if the two collide.
        return objRect1.intersects(objRect2);
    }

    /**
     * Method for detecting the collisions between objects reside in the game.
     * gameState parameter is added for updating the score of the user.
     *
     * @param gameState
     */
    public void detectCollisions(GameState gameState) {
        // IF a userBullet collides with an alien.
        for (BaseGameObject userbullet : userBullets) {
            if (!userbullet.isDead())
                for (BaseGameObject alien : aliens)
                    if (!alien.isDead() && intersects(userbullet, alien))
                        alien.shotBy(userbullet, gameState);
        }
        // IF an alien bullet collides with the spaceship.
        for (BaseGameObject alienbullet : alienBullets) {
            if (!alienbullet.isDead() && intersects(alienbullet, spaceShip))
                spaceShip.shotBy(alienbullet, gameState);
        }
        // IF an alien and the spaceship collide -> destroy both of them.
        // Game is OVER.
        for (BaseGameObject alien : aliens) {
            if (!alien.isDead() && intersects(alien, spaceShip)) {
                spaceShip.setDead();
                alien.setDead();
            }
        }

    }

    private void MouseMoveEventHandle(MouseEvent mouseEvent) {
        double lastMousePositionX = mouseEvent.getX();
        double lastMousePositionY = mouseEvent.getY();
        spaceShip.setMousePositionY(lastMousePositionY);
        spaceShip.setMousePositionX(lastMousePositionX);
    }

    void initialize(GameState gameState) {
        myMouseMoveEventHandler = new MouseMoveEventHandler(this);
        createSpaceShip(gameState);
    }


    private class MouseMoveEventHandler implements EventHandler<MouseEvent> {

        final AbstractLevel superClass;

        MouseMoveEventHandler(AbstractLevel superClass) {
            this.superClass = superClass;
        }

        @Override
        public void handle(MouseEvent mouseEvent) {
            superClass.MouseMoveEventHandle(mouseEvent);
        }
    }

}
