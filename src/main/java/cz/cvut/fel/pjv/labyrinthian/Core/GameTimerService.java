package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.Components.GameStats;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Background service that updates the current level time every second.
 * Runs on a separate thread to avoid blocking the JavaFX Application Thread.
 */
public class GameTimerService extends Service<Long> {

    private static final long UPDATE_INTERVAL_MS = 1000;

    private final GameStats gameStats;

    /**
     * Creates a new timer service linked to the given game statistics.
     *
     * @param gameStats the stats object to read the level start time from
     */
    public GameTimerService(GameStats gameStats) {
        this.gameStats = gameStats;
    }

    /**
     * Creates the background task that updates the elapsed level time every second.
     * @return a task that continuously updates the current level time
     */
    @Override
    protected Task<Long> createTask() {
        return new Task<>() {
            @Override
            protected Long call() throws Exception {
                while (!isCancelled()) {
                    // updateValue is thread-safe — it posts the value to the Application Thread
                    updateValue(gameStats.getCurrentLevelTime());
                    Thread.sleep(UPDATE_INTERVAL_MS);
                }
                return gameStats.getCurrentLevelTime();
            }
        };
    }
}
