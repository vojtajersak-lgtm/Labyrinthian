package cz.cvut.fel.pjv.labyrinthian.UI;

import cz.cvut.fel.pjv.labyrinthian.Components.Utils;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Core.GameState;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller for the main menu, allows player to start a new game or to load and start a saved game
 * or to exit the application.
 */
public class MainMenuScreen {
    private Stage stage;
    private Scene gameScene;
    private AnimationTimer timer;
    private GameManager gameManager;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public void setTimer(AnimationTimer timer) {
        this.timer = timer;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @FXML
    public void onStartGame() {
        gameManager.startNewGame();
        stage.setScene(gameScene);
        gameManager.setCurrentState(GameState.RUNNING);

        timer.start();
    }

    @FXML
    public void onLoadGame(){
        if(gameManager.getSaveManager().loadGame(gameManager)){
            Utils.switchScene(stage,gameScene);
            gameManager.setCurrentState(GameState.RUNNING);
            timer.start();
        }

    }

    @FXML
    public void onExit() {
        stage.close();
    }
}
