package cz.cvut.fel.pjv.labyrinthian.Components;

public class GameStats {
    int levelsCompleted;
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

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public long getLevelStartTime() {
        return levelStartTime;
    }

    public void setLevelStartTime(long levelStartTime) {
        this.levelStartTime = levelStartTime;
    }

    public double getAverageLevelTime() {
        return averageLevelTime;
    }

    public void setAverageLevelTime(double averageLevelTime) {
        this.averageLevelTime = averageLevelTime;
    }

    public int getLevelsCompleted() {
        return levelsCompleted;
    }

    public void setLevelsCompleted(int levelsCompleted) {
        this.levelsCompleted = levelsCompleted;
    }

    public long getCurrentLevelTime() {
        return (System.currentTimeMillis() - levelStartTime) / 1000;
    }

    public void addKillScore(boolean isBoss, boolean isTransformed) {
        if (isBoss) totalScore += 10 * currentLevel * 500;
        else if (isTransformed) totalScore += 10 * currentLevel * 1000;
        else totalScore += 10 * currentLevel * 50;
    }

    public void completeLevelScore() {
        long levelTime = getCurrentLevelTime();
        totalScore += (int) (Math.max(0, 60 - levelTime) * 10);
        averageLevelTime = (averageLevelTime * levelsCompleted + levelTime) / (levelsCompleted + 1);
        levelsCompleted++;
    }

    public void resetLevel() {
        levelStartTime = System.currentTimeMillis();
        currentLevel++;
    }


}
