package com.example.game.objects;

import com.example.game.GameEnums;
import com.example.game.levelsAndServices.GameState;
import javafx.scene.image.Image;

public class BossAlien extends BaseGameObject {
    @Override
    public void shotBy(BaseGameObject attacker, GameState gameState) {
        // Bullets must be cleared from the scene when they hit their targets.
        attacker.setDead();
        // Health of the damaged object should be decreased.
        healthPoints -= attacker.getDamage();

        if (healthPoints <= 0) {
            setDead();
            }
    }

    private static final double speedX = 10.0;

    public BossAlien(Image sprite, int width, int height) {
        super(sprite, width, height);
        setReward(GameEnums.BOSS_ALIEN_REWARD);
        setDamage(GameEnums.BOSS_ALIEN_DMG);
        setVelocityX(speedX);
    }

    public void update(double elapsedTime) {
        setPosX( getVelocityX() * elapsedTime + getPosX());
        // Change direction on the boundaries
        if( getPosX()+getWidth() < 0 || getPosX() > GameEnums.SCREEN_WIDTH)
            setVelocityX(-speedX);
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
