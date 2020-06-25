package com.example.game.levelsAndServices;

import com.example.game.GameEnums;
import com.example.game.objects.BaseGameObject;

import java.util.LinkedList;

public class Level2 extends AbstractLevel {

    public Level2() {
        generateAliens();
    }

    // Generate all aliens at the beginning and locate them properly on the scene.
    private void generateAliens() {
        int offsetX = 100, offsetY = 30;
        int gapX = 150, gapY = 100;
        int alienInEachRow = 5;
        int rows = 2;

        LinkedList<BaseGameObject> mediumAliens = AbstractLevel.createAliens(GameEnums.MediumAlien, alienInEachRow, rows, offsetX, gapX, offsetY, gapY);
        aliens.addAll(mediumAliens);
    }

}
