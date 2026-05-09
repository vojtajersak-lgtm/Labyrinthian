package cz.cvut.fel.pjv.labyrinthian.UI;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Core.GameState;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PauseMenuScreen {
    private  Stage stage;
    private  Scene menuScene;
    private AnimationTimer timer;
    private  GameManager gameManager;
    private Parent pauseMenuRoot;

    public void setStage(Stage stage) {this.stage = stage;
    }


    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void setPauseMenuRoot(Parent pauseMenuRoot) {
        this.pauseMenuRoot = pauseMenuRoot;
    }

    public void setMenuScene(Scene menuScene) {
        this.menuScene = menuScene;
    }

    public void setTimer(AnimationTimer timer) {
        this.timer = timer;
    }

    @FXML
    public void onResumeGame(){
        pauseMenuRoot.setVisible(false);
        gameManager.setCurrentState(GameState.RUNNING);
    }
    @FXML
    public void onSaveGame() {
        //save game
    }

    @FXML
    public void onSettings() {
        //open settings
    }

    @FXML
    public void onMainMenu() {
        stage.setScene(menuScene);
        timer.stop();
        gameManager.setCurrentState(GameState.MAIN_MENU);
        pauseMenuRoot.setVisible(false);
    }


}
