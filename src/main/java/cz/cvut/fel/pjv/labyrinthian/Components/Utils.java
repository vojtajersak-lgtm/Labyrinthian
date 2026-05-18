package cz.cvut.fel.pjv.labyrinthian.Components;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Utility class with static helper methods shared across the game.
 */
public class Utils {

    /**
     * Calculates the Manhattan distance between two points in pixel coordinates.
     * Manhattan distance is the sum of absolute differences in x and y axes,
     * giving the distance "as walked" rather than straight-line distance.
     *
     * @param x1 x coordinate of the first point
     * @param y1  coordinate of the first point
     * @param x2 x coordinate of the second point
     * @param y2 y coordinate of the second point
     * @return Manhattan distance in pixels
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    /**
     * Switches the scene while keeping window dimensions
     * Used to prevent the window from resizing when changing between scenes.
     *
     * @param stage    the JavaFX stage
     * @param newScene the scene to switch to
     */
    public static void switchScene(Stage stage, Scene newScene) {
        double w = stage.getWidth();
        double h = stage.getHeight();
        stage.setScene(newScene);
        stage.setWidth(w);
        stage.setHeight(h);
    }
}
