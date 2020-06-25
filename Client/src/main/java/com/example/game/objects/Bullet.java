package com.example.game.objects;

import com.example.game.GameEnums;
import javafx.scene.image.Image;

/**
 * Bullet class for bullets of both space ship and alien objects.
 */
public class Bullet extends BaseGameObject {

    public Bullet(Image sprite, int width, int height) {
        super(sprite, width, height);
        setVelocityX(0); // Bullets only move on the y axis.
    }

    /**
     * Change position of the bullet, if it quits from either side of the screen (top or bottom) delete it.
     * @param elapsedTime
     */
    public void update(double elapsedTime) {
        setPosY( getPosY() + getVelocityY()*elapsedTime );
        setPosX( getPosX() + getVelocityX()*elapsedTime );
        if( getPosY()+getHeight() < 0 || getPosY() > GameEnums.SCREEN_HEIGHT)
            setDead(); // delete bullet from the screen
    }
}
