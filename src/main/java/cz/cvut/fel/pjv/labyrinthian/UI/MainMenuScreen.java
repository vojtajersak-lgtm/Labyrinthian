package cz.cvut.fel.pjv.labyrinthian.UI;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuScreen {
    private final Stage stage;
    private final Scene scene;
    private final GameManager gameManager;

    public MainMenuScreen(Stage stage, GameManager gameManager) {
        this.stage = stage;
        this.scene = null; // temporary
        this.gameManager = gameManager;

    }

    public void show(){
        stage.setScene(scene);
    }
}
