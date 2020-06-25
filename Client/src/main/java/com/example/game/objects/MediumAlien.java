package com.example.game.objects;

import com.example.game.GameEnums;
import javafx.scene.image.Image;

public class MediumAlien extends BaseGameObject {
    private static final double speedY = 15.0;

    public MediumAlien(Image sprite, int width, int height) {
        super(sprite, width, height);
        setReward(GameEnums.MEDIUM_ALIEN_REWARD);
        setVelocityY(speedY);
    }

    public void update(double elapsedTime) {
        setPosY( getVelocityY() * elapsedTime + getPosY()); // It doesn't move on x axis.
    }
}
