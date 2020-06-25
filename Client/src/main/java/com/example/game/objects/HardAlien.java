package com.example.game.objects;

import com.example.game.GameEnums;
import javafx.scene.image.Image;

public class HardAlien extends BaseGameObject {

    private static final double speedY = 15.0;

    public HardAlien(Image sprite, int width, int height) {
        super(sprite, width, height);
        setReward(GameEnums.HARD_ALIEN_REWARD);
        setVelocityY(speedY);
        // For shooting
        setDamage(GameEnums.HARD_ALIEN_DMG);
    }

    public void update(double elapsedTime) {
        setPosY( getVelocityY() * elapsedTime + getPosY()); // It doesn't move on x axis.
    }
    public Bullet shoot() {
        Bullet bullet = new Bullet(GameEnums.ALIEN_BULLET_IMAGE,GameEnums.ALIEN_BULLET_WIDTH,GameEnums.ALIEN_BULLET_HEIGHT);
        bullet.setVelocityY(GameEnums.ALIEN_BULLET_VELOCITY);
        bullet.setDamage(getDamage());
        //bullet.setHealthPoints(1); // check the health of bullet when collision happens.
        bullet.setPos(getPosX() + getWidth() / 2.0, getPosY()); // check position again??
        return bullet;
    }
}
