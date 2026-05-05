package cz.cvut.fel.pjv.labyrinthian.Components;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameStatsTest {
    @Test
    void testAddKillScore_normalEnemy() {
        GameStats stats = new GameStats();
        stats.addKillScore(false, false);
        assertEquals(500, stats.getTotalScore());
    }

    @Test
    void testAddKillScore_Boss() {
        GameStats stats = new GameStats();
        stats.addKillScore(true, false);
        assertEquals(5000, stats.getTotalScore());
    }

    @Test
    void testAddKillScore_transformed() {
        GameStats stats = new GameStats();
        stats.addKillScore(false, true);
        assertEquals(10000, stats.getTotalScore());
    }

    @Test
    void testCompleteLevelScore_noBonus() {
        GameStats stats = new GameStats();
        stats.setLevelStartTime(System.currentTimeMillis() - 70000);
        stats.setTotalScore(100);
        stats.completeLevelScore();
        assertEquals(100, stats.getTotalScore());

    }

    @Test
    void testCompleteLevelScore_Bonus() {
        GameStats stats = new GameStats();
        stats.setLevelStartTime(System.currentTimeMillis());
        stats.setTotalScore(100);
        stats.completeLevelScore();
        assertTrue(stats.getTotalScore() > 500 + 100);

    }

    @Test
    void testLevelReset_levelIncreases(){
        GameStats stats = new GameStats();
        stats.resetLevel();
        assertEquals(2, stats.getCurrentLevel());
    }


    @Test
    void testLevelReset_startTimeIncreases() throws InterruptedException {
        GameStats stats = new GameStats();
        long oldTime = stats.getLevelStartTime();
        Thread.sleep(10);
        stats.resetLevel();
        assertTrue(oldTime < stats.getLevelStartTime());
    }
}
