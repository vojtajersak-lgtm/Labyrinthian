package cz.cvut.fel.pjv.labyrinthian.Components;

/**
 * Tracks game statistics - score, level progress and level timing.
 * Used by the HUD, end screen and save system.
 */
public class GameStats {
    private int levelsCompleted;
    private int currentLevel;
    private int totalScore;
    private long levelStartTime;
    private double averageLevelTime;


    public GameStats() {
        this.currentLevel = 1;
        this.totalScore = 0;
        this.levelStartTime = System.currentTimeMillis();
        this.averageLevelTime = 0;
        this.levelsCompleted = 0;
    }

    public int getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }
    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }
    public long getLevelStartTime() { return levelStartTime; }
    public void setLevelStartTime(long levelStartTime) { this.levelStartTime = levelStartTime; }
    public double getAverageLevelTime() { return averageLevelTime; }
    public void setAverageLevelTime(double averageLevelTime) { this.averageLevelTime = averageLevelTime; }
    public int getLevelsCompleted() { return levelsCompleted; }
    public void setLevelsCompleted(int levelsCompleted) { this.levelsCompleted = levelsCompleted; }

    /**
     * Returns elapsed time on the current level in seconds.
     *
     * @return seconds since the current level started
     */
    public long getCurrentLevelTime() {
        return (System.currentTimeMillis() - levelStartTime) / 1000;
    }

    @Override
    public String toString() {
        return "Levels Completed: " + levelsCompleted +
               "\n\nTotal Score: " + totalScore +
               "\n\nAverage Level Time: " + averageLevelTime;
    }

    /**
     * Adds score for killing an enemy.
     *
     * @param isBoss       true if the killed entity was the boss, awards extra points
     * @param isTransformed true if the boss was transformed, awards most points
     */
    public void addKillScore(boolean isBoss, boolean isTransformed) {
        if (isBoss) totalScore += 10 * currentLevel * 500;
        else if (isTransformed) totalScore += 10 * currentLevel * 1000;
        else totalScore += 10 * currentLevel * 50;
    }

    /**
     * Calculates and adds a time bonus for completing the level quickly,
     * then updates the running average level time.
     */
    public void completeLevelScore() {
        long levelTime = getCurrentLevelTime();
        // Bonus decreases the longer the level takes; no bonus after 150 seconds
        totalScore += (int) (Math.max(0, 150 - levelTime) * 50);
        averageLevelTime = (averageLevelTime * levelsCompleted + levelTime) / (levelsCompleted + 1);
        levelsCompleted++;
    }

    /**
     * Resets the level timer and increments the level counter.
     */
    public void resetLevel() {
        levelStartTime = System.currentTimeMillis();
        currentLevel++;
    }
}
