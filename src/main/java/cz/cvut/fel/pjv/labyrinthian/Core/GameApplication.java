package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.UI.DialogScreen;
import cz.cvut.fel.pjv.labyrinthian.UI.MainMenuScreen;
import cz.cvut.fel.pjv.labyrinthian.UI.PauseMenuScreen;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameApplication extends Application {




    private static final Logger LOG = LoggerFactory.getLogger(GameApplication.class);
    private final Canvas canvas = new Canvas(1024, 576);


    @Override
    public void start(Stage stage) throws Exception {
        Font myFont = Font.loadFont(getClass().getResourceAsStream("/fonts/StarCrush.ttf"), 14);
        // logging setup
        String level = System.getProperty("logLevel", "INFO");
        ch.qos.logback.classic.Logger rootLogger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(ch.qos.logback.classic.Level.valueOf(level));

        InputManager inputManager = new InputManager();
        GameManager gameManager = new GameManager(inputManager);
        gameManager.getTimerService().start();

        // Game scene
        StackPane gameRoot = new StackPane();
        gameRoot.getChildren().add(canvas);

        FXMLLoader pauseLoader = new FXMLLoader(getClass().getResource("/PauseMenu.fxml"));
        Parent pauseMenuRoot = pauseLoader.load();
        pauseMenuRoot.setVisible(false);
        gameRoot.getChildren().add(pauseMenuRoot);

        FXMLLoader dialogLoader = new FXMLLoader(getClass().getResource("/Dialog.fxml"));
        Parent dialogMenuRoot = dialogLoader.load();
        dialogMenuRoot.setVisible(false);
        gameRoot.getChildren().add(dialogMenuRoot);

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
                switch (gameManager.getCurrentState()){

                    case RUNNING -> gameManager.update();
                    case LEVEL_COMPLETE -> gameManager.nextLevel();
                    case PAUSED -> {


                        pauseMenuRoot.setVisible(true);

                    }
                    case DIALOGUE -> {
                        //dialogue
                    }
                    case GAME_OVER -> {
                        //draw game over screen, show score, exit to menu button, stop timer?
                    }
                    case WON -> {
                        //show win scree, show score, offer continuing into endless mode or return into main menu
                    }
                }



                renderer.render(gc, gameManager.getGamestats().getCurrentLevelTime(),
                        gameManager.getGamestats().getTotalScore(), gameManager.getMap(),
                        gameManager.getMainCharacter(), gameManager.getEscapePortal(),
                        gameManager.getEnemyList(), gameManager.getBoss(),
                        gameManager.getProjectiles(), gameManager.getClayPots(),
                        gameManager.getLooseItemList(), gameManager.isMapMode(),
                        gameManager.isBlindingStewActive());
            }
        };

        // Menu scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainMenu.fxml"));
        Parent menuRoot = loader.load();
        Scene menuScene = new Scene(menuRoot, 1024, 576);


        MainMenuScreen menuScreen = loader.getController();
        menuScreen.setStage(stage);
        menuScreen.setGameScene(gameScene);
        menuScreen.setTimer(timer);
        menuScreen.setGameManager(gameManager);

        PauseMenuScreen pauseScreen = pauseLoader.getController();
        pauseScreen.setPauseMenuRoot(pauseMenuRoot);
        pauseScreen.setStage(stage);
        pauseScreen.setMenuScene(menuScene);
        pauseScreen.setTimer(timer);
        pauseScreen.setGameManager(gameManager);

        DialogScreen dialogScreen = dialogLoader.getController();
        dialogScreen.setGameManager(gameManager);
        gameManager.setDialogScreen(dialogScreen);



        stage.setTitle("Labyrinthian");
        stage.setScene(menuScene);
        stage.show();
    }
}