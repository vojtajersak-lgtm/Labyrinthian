package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.UI.DialogScreen;
import cz.cvut.fel.pjv.labyrinthian.UI.EndScreen;
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
    private final Font myFont = Font.loadFont(getClass().getResourceAsStream("/fonts/StarCrush.ttf"), 14);

    /**
     * Starts the application
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
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

        // Scene loading
        StackPane gameRoot = new StackPane();
        gameRoot.getChildren().add(canvas);
        canvas.widthProperty().bind(gameRoot.widthProperty());
        canvas.heightProperty().bind(gameRoot.heightProperty());

        FXMLLoader endLoader = new FXMLLoader(getClass().getResource("/GameOver.fxml"));
        Parent endLoaderRoot = endLoader.load();
        Scene endScene = new Scene(endLoaderRoot);
        EndScreen endScreen  = endLoader.getController();


        FXMLLoader pauseLoader = new FXMLLoader(getClass().getResource("/PauseMenu.fxml"));
        Parent pauseMenuRoot = pauseLoader.load();
        pauseMenuRoot.setVisible(false);
        gameRoot.getChildren().add(pauseMenuRoot);

        FXMLLoader dialogLoader = new FXMLLoader(getClass().getResource("/Dialog.fxml"));
        Parent dialogMenuRoot = dialogLoader.load();
        dialogMenuRoot.setVisible(false);
        gameRoot.getChildren().add(dialogMenuRoot);

        Scene gameScene = new Scene(gameRoot);
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
                if (now - lastUpdate < 13_333_333) return;
                lastUpdate = now;

                //  Game state machine

                switch (gameManager.getCurrentState()) {

                    case RUNNING -> gameManager.update();
                    case LEVEL_COMPLETE -> gameManager.nextLevel();
                    case PAUSED -> {


                        pauseMenuRoot.setVisible(true);

                    }
                    case DIALOGUE -> {
                        //dialogue
                    }
                    case GAME_OVER -> {
                        endScreen.showGameOver(gameManager.getGamestats());
                        stage.setScene(endScene);
                        gameManager.setCurrentState(GameState.SHOWING_END);
                    }
                    case WON -> {
                        endScreen.showWin(gameManager.getGamestats());
                        stage.setScene(endScene);
                        gameManager.setCurrentState(GameState.SHOWING_END);
                    }
                    case SHOWING_END -> {
                    }


                }

                // Renders the whole game
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
        Scene menuScene = new Scene(menuRoot);


        // initializes all scenes and controllers
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


        endScreen.setStage(stage);
        endScreen.setMenuScene(menuScene);
        endScreen.setTimer(timer);
        endScreen.setGameManager(gameManager);
        endScreen.setGameScene(gameScene);


        DialogScreen dialogScreen = dialogLoader.getController();
        dialogScreen.setGameManager(gameManager);
        gameManager.setDialogScreen(dialogScreen);


        stage.setTitle("Labyrinthian");
        stage.setScene(menuScene);
        stage.show();
    }
}