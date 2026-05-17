package cz.cvut.fel.pjv.labyrinthian.Components;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Utils {
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2); // Manhattan
    }

    public static void switchScene(Stage stage, Scene newScene) {
        double w = stage.getWidth();
        double h = stage.getHeight();
        stage.setScene(newScene);
        stage.setWidth(w);
        stage.setHeight(h);
    }
}
