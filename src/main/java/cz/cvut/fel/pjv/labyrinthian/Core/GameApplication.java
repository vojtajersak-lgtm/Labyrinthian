package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import cz.cvut.fel.pjv.labyrinthian.World.WorldBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameApplication extends Application {

    private final Canvas canvas = new Canvas(1024, 576);


    @Override
    public void start(Stage stage) throws Exception {

        InputManager inputManager = new InputManager();
        GameManager gameManager = new GameManager(inputManager);

        StackPane root = new StackPane();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, 1024, 576);
        stage.setScene(scene);
        stage.setTitle("Labyrinthian");
        scene.setOnKeyPressed(e -> {inputManager.setLastCode(e.getCode());
                                            inputManager.setLastPressed(e.getCode());});
        scene.setOnKeyReleased(e -> inputManager.removeLastCode(e.getCode()));

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Renderer renderer = new Renderer();

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {

                if( now - lastUpdate < 150_000_000) return;
                lastUpdate = now;
                gameManager.update();
                renderer.render(gc,gameManager.getMap(),
                        gameManager.getMainCharacter(), gameManager.getEnemyList(), gameManager.isMapMode());
            }
        };

        timer.start();
        stage.show();
    }
}
