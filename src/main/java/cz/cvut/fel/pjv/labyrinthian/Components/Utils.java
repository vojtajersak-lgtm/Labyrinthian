package cz.cvut.fel.pjv.labyrinthian.Components;

public class Utils {
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2); // Manhattan
    }
}
