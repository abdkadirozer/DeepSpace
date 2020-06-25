package com.example.game.objects;

import com.example.game.GameEnums;
import com.example.game.levelsAndServices.GameState;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * This is the base class for all existing objects in our game.
 * In other words, each interactable object such as the spaceship or an alien is a BaseGameObject.
 */
public abstract class BaseGameObject {

    protected double posX, posY;
    protected double width, height;
    protected double velocityX, velocityY;
    protected int damage;
    protected int healthPoints;
    protected int reward; // User gets rewarded for eliminating alien ships, i.e., increases his/her score in the game.
    protected boolean isDead; // Flag to keep tracing if objects are dead or alive.
    protected final Image sprite;

    public BaseGameObject(Image sprite, int width, int height) {
        this.sprite = sprite;
        this.posX = 0;
        this.posY = 0;
        this.damage = 0;
        this.healthPoints = 0;
        this.width = width;
        this.height = height;
        this.velocityX = 0;
        this.velocityY = 0;
        this.isDead = false;
    }

    public Image getSprite() {
        return sprite;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setPos(double x, double y) {
        this.posX = x;
        this.posY = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead() {
        isDead = true;
    }

    /**
     * When a collision occurs between two objects, this method will be called.
     * @param attacker
     * @param gameState
     */
    public void shotBy(BaseGameObject attacker, GameState gameState) {
        // Bullets must be cleared from the scene when they hit their targets.
        attacker.setDead();
        // Health of the damaged object should be decreased.
        healthPoints -= attacker.getDamage();

        // When the spaceship is shot, Health which is shown on the screen is updated.
        if(this.getSprite().equals(GameEnums.SPACE_SHIP_IMG))
        {
            gameState.setHealth(healthPoints);
        }
        if (healthPoints <= 0) {
            setDead();
            gameState.updateScore(getReward());
        }
    }

    public void render(GraphicsContext context) {
        context.drawImage(sprite, posX, posY, width, height); // Drawing image of the game object.
    }

    public abstract void update(double elapsedTime);

}
