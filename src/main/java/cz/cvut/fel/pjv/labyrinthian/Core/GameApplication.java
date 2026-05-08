package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import cz.cvut.fel.pjv.labyrinthian.World.WorldBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameApplication extends Application {




    private static final Logger LOG = LoggerFactory.getLogger(GameApplication.class);
    private final Canvas canvas = new Canvas(1024, 576);


    @Override
    public void start(Stage stage) throws Exception {
        // logging setup
        String level = System.getProperty("logLevel", "INFO");
        ch.qos.logback.classic.Logger rootLogger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(ch.qos.logback.classic.Level.valueOf(level));

        InputManager inputManager = new InputManager();
        GameManager gameManager = new GameManager(inputManager);
        gameManager.getTimerService().start();

        // Herní scéna
        StackPane gameRoot = new StackPane();
        gameRoot.getChildren().add(canvas);
        Scene gameScene = new Scene(gameRoot, 1024, 576);
        gameScene.setOnKeyPressed(e -> {
            inputManager.setLastCode(e.getCode());
            inputManager.setLastPressed(e.getCode());
            inputManager.getJustReleased().clear();
        });
        gameScene.setOnKeyReleased(e -> {
            inputManager.removeLastCode(e.getCode());
            inputManager.setJustReleased(e.getCode());
        });

        GraphicsContext gc = canvas.getGraphicsContext2D();
        Renderer renderer = new Renderer(gameManager.getYarnBallTrail());

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if(now - lastUpdate < 13_333_333) return;
                lastUpdate = now;
                gameManager.update();
                renderer.render(gc, gameManager.getGamestats().getCurrentLevelTime(),
                        gameManager.getGamestats().getTotalScore(), gameManager.getMap(),
                        gameManager.getMainCharacter(), gameManager.getEscapePortal(),
                        gameManager.getEnemyList(), gameManager.getBoss(),
                        gameManager.getProjectiles(), gameManager.getClayPots(),
                        gameManager.getLooseItemList(), gameManager.isMapMode(),
                        gameManager.isBlindingStewActive());
            }
        };

        // Menu scéna
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainMenu.fxml"));
        Parent menuRoot = loader.load();
        Scene menuScene = new Scene(menuRoot, 1024, 576);

        MainMenuController controller = loader.getController();
        controller.setStage(stage);
        controller.setGameScene(gameScene);
        controller.setTimer(timer);

        stage.setTitle("Labyrinthian");
        stage.setScene(menuScene);
        stage.show();
    }
}