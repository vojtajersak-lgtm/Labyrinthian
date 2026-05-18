package cz.cvut.fel.pjv.labyrinthian.UI;

import cz.cvut.fel.pjv.labyrinthian.Components.GameStats;
import cz.cvut.fel.pjv.labyrinthian.Components.Utils;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Core.GameState;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.scene.control.Label;

/**
 * Controller that shows the post game screen after winning/ dying
 */
public class EndScreen {

    @FXML
    private Label stats;

    @FXML
    private Label title;

    @FXML private Button continueButton;

    @FXML private AnchorPane root;

    @FXML private Label messageText;



    private Stage stage;

    private Scene menuScene;

    private Scene gameScene;

    private AnimationTimer timer;

    private GameManager gameManager;


    public void showGameOver(GameStats statistics) {
        root.setId("gameOverRoot");
        title.setText("GAME OVER");
        title.getStyleClass().remove("won");
        stats.setText(statistics.toString());
        stats.getStyleClass().remove("won");
        messageText.setVisible(false);
        messageText.setManaged(false);
        continueButton.setVisible(false);
        continueButton.setManaged(false);
        timer.stop();
    }


    public void showWin(GameStats statistics) {
        root.setId("gameWonRoot");
        title.setText("YOU WIN!");
        title.getStyleClass().add("won");
        stats.setText(statistics.toString());
        stats.getStyleClass().add("won");
        messageText.setVisible(true);
        messageText.setManaged(true);
        continueButton.setVisible(true);
        continueButton.setManaged(true);
        timer.stop();
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMenuScene(Scene menuScene) {
        this.menuScene = menuScene;
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
    public void onReturnToMenu() {
        Utils.switchScene(stage,menuScene);
        gameManager.setCurrentState(GameState.MAIN_MENU);
    }

    /**
     * switches game to endless mode - player continues to play through levels until they eventually die (or get bored).
     * Thanks to procedural generation of levels, player can theoretically continue on indefinitely
     */
    @FXML
    public void onEndlessMode(){
        Utils.switchScene(stage,gameScene);
        gameManager.setCurrentState(GameState.RUNNING);
        timer.start();
    }





}
