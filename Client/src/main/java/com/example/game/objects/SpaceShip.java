package com.example.game.objects;

import com.example.game.GameEnums;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SpaceShip extends BaseGameObject {

    private double mousePositionX;

    private double mousePositionY;
    private static final double spaceShipSpeed = 3; // randomly assigned, might be changed later.

    //Using only multiplayer level
    private int score =0;
    private String username ="";


    public double getMousePositionY() {
        return mousePositionY;
    }

    public void updateScore(int reward)
    {
        this.score += reward;
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SpaceShip(Image sprite, int width, int height) {
        super(sprite, width, height);
    }

    public void update(double elapsedTime) {

        setVelocityX( -spaceShipSpeed*(getPosX() - getMousePositionX() + getWidth()/2.0 ) );
        setVelocityY(-spaceShipSpeed * (getPosY() - getMousePositionY() + getWidth() / 2.0));


        if( (getVelocityY()*elapsedTime + getPosY()) > GameEnums.SCREEN_HEIGHT/2 + (GameEnums.SPACE_SHIP_HEIGHT/2) && (getVelocityY()*elapsedTime + getPosY()) < GameEnums.SCREEN_HEIGHT - (GameEnums.SPACE_SHIP_HEIGHT /2) ){
            setPosY((getVelocityY()*elapsedTime + getPosY()));
        }// set new position of the spaceship.

        setPosX( (getVelocityX()*elapsedTime + getPosX()));
        }

    // Shoot a bullet.
    public Bullet shoot() {
        Bullet bullet = new Bullet(GameEnums.USER_BULLET_IMG,GameEnums.USER_BULLET_WIDTH,GameEnums.USER_BULLET_HEIGHT);
        bullet.setVelocityY(GameEnums.USER_BULLET_VELOCITY);
        bullet.setDamage(getDamage());
        bullet.setHealthPoints(1); // check the health of bullet when collision happens.
        bullet.setPos(getPosX() + getWidth() / 2.0, getPosY());
        return bullet;
    }

    public void render(GraphicsContext context) {
            context.save();
            context.drawImage(getSprite(), getPosX(), getPosY(), getWidth(), getHeight()); // Drawing image of the space ship.
            context.restore();
    }

    public double getMousePositionX() {
        return mousePositionX;
    }
    public void setMousePositionX(double mousePositionX) {
        this.mousePositionX = mousePositionX;
    }

    public void setMousePositionY(double mousePositionY){
        this.mousePositionY = mousePositionY;
    }

}
