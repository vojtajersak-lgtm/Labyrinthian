package cz.cvut.fel.pjv.labyrinthian.Core;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {
    private Stage stage;
    private Scene gameScene;
    private AnimationTimer timer;

    public void setStage(Stage stage) { this.stage = stage; }
    public void setGameScene(Scene gameScene) { this.gameScene = gameScene; }
    public void setTimer(AnimationTimer timer) { this.timer = timer; }

    @FXML
    public void onStartGame() {
        stage.setScene(gameScene);
        timer.start();
    }

    @FXML
    public void onExit() {
        stage.close();
    }
}
