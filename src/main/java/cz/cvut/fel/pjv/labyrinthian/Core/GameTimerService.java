package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.Components.GameStats;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class GameTimerService extends Service<Long> {
    private final GameStats gameStats;

    public GameTimerService(GameStats gameStats) {
        this.gameStats = gameStats;
    }

    @Override
    protected Task<Long> createTask() {
        return new Task<>() {
            @Override
            protected Long call() throws Exception {
                while (!isCancelled()) {
                    updateValue(gameStats.getCurrentLevelTime());
                    Thread.sleep(1000);
                }
                return gameStats.getCurrentLevelTime();
            }
        };
    }
}
