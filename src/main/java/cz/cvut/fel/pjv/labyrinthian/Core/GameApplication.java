package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.World.Map;
import cz.cvut.fel.pjv.labyrinthian.World.WorldBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameApplication extends Application {

    private Canvas canvas = new Canvas(640, 640);


    @Override
    public void start(Stage stage) throws Exception {

        Map map = new WorldBuilder().buildMap(32);

        StackPane root = new StackPane();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, 640, 640);
        stage.setScene(scene);
        stage.setTitle("Labyrinthian - Semestrální práce");

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Renderer renderer = new Renderer();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                renderer.render(gc, map);
            }
        };

        timer.start();
        stage.show();
    }
}
