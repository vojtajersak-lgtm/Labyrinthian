package cz.cvut.fel.pjv.labyrinthian.Entities;

public abstract class GameObject {
    protected double cordY, cordX;
    protected double width, height;

    public GameObject(double cordY, double cordX) {
        this.cordY = cordY;
        this.cordX = cordX;
    }

    public GameObject(double cordX, double cordY, double width, double height) {
        this.cordY = cordY;
        this.cordX = cordX;
        this.width = width;
        this.height = height;
    }

    public double getCordY() {
        return cordY;
    }

    public double getCordX() {
        return cordX;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
