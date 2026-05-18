package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.Entities.Boss;
import cz.cvut.fel.pjv.labyrinthian.Entities.Enemy;

import java.util.List;

/**
 * Data transfer object for game state saving
 * The map is stored as an integer matrix (1=PATH, 2=HEDGE, 3=ARENA_WALL).
 */
public class SaveData {
    // Player position and stats
    public double playerX;
    public double playerY;
    public double playerCurrentHp;
    public String playerActiveWeapon;
    public double playerMaxHp;
    public double playerBaseDamage;   // defaultValues[2]
    public double playerAttackRange;  // defaultValues[3]
    public boolean lifeStealActive;

    /** Item names for each inventory slot (5 entries); "EMPTY" for empty slots. */
    public List<String> inventoryItemNames;

    /**  Clay pot position data for save/load. */
    public static class ClayPotData {
        public double x;
        public double y;
    }

    public List<ClayPotData> savedClayPots;

    // Game statistics
    public int currentLevel;
    public int levelsCompleted;
    public int totalScore;
    public long elapsedLevelTimeMillis;
    public double averageLevelTime;

    // Active game flags
    public boolean yarnBallActive;
    public boolean blindingStewActive;
    public boolean hasObliterator;
    public double speedMultiplier;

    // Entity state
    public List<Enemy> enemyList;
    public Boss boss;

    // Portal state
    public boolean portalExists;
    public double portalX;
    public double portalY;

    /** Map tile matrix - encoded as integers to save space */
    public int[][] mapMatrix;
}
