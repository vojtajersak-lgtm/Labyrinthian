package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.Entities.Boss;
import cz.cvut.fel.pjv.labyrinthian.Entities.ClayPot;
import cz.cvut.fel.pjv.labyrinthian.Entities.Enemy;
import cz.cvut.fel.pjv.labyrinthian.Items.LooseItem;

import java.util.List;

public class SaveData {
    public double playerX;
    public double playerY;
    public double playerCurrentHp;
    public String playerActiveWeapon;
    public double playerMaxHp;
    public double playerSpeedMultiplier;
    public double playerBaseDamage; // defaultValues[2]
    public double playerAttackRange; // defaultValues[3]
    public boolean lifeStealActive;

    public List<String> inventoryItemNames;

    public static class ClayPotData {
        public double x;
        public double y;
    }


    public List<ClayPotData> savedClayPots;

    public int currentLevel;
    public int levelsCompleted;
    public int totalScore;
    public long elapsedLevelTimeMillis;
    public double averageLevelTime;
    public boolean yarnBallActive;
    public boolean blindingStewActive;
    public boolean hasObliterator;
    public double speedMultiplier;

    public List<Enemy> enemyList;
    public Boss boss;


    public boolean portalExists;
    public double portalX;
    public double portalY;

    public int[][] mapMatrix;
}