package com.example.game.levelsAndServices;


import com.example.game.GameEnums;
import com.example.game.objects.BaseGameObject;

import java.util.LinkedList;

public class Level1 extends AbstractLevel {

    public Level1() {
        generateAliens();
    }
    // Generate all aliens at the beginning and locate them properly on the scene.
    private void generateAliens() {
        int offsetX = 100, offsetY = 30;
        int gapX = 150, gapY = 100;
        int alienInEachRow = 5;
        int rows = 3;

        LinkedList<BaseGameObject> easyAliens = AbstractLevel.createAliens(GameEnums.EasyAlien, alienInEachRow, rows, offsetX, gapX, offsetY, gapY);
        aliens.addAll(easyAliens);
    }
}
