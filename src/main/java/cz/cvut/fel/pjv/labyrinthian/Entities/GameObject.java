package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.World.Map;

public abstract class GameObject {
    protected double cordY, cordX;
    protected double width, height;

    public GameObject(double cordX, double cordY) {
        this.cordY = cordY;
        this.cordX = cordX;
    }

    public GameObject(double cordX, double cordY, double height, double width) {
        this.cordY = cordY;
        this.cordX = cordX;
        this.width = width;
        this.height = height;
    }

    public double getCordY() {
        return cordY;
    }

    public void setCordY(double cordY) {
        this.cordY = cordY;
    }

    public double getCordX() {
        return cordX;
    }

    public void setCordX(double cordX) {
        this.cordX = cordX;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getCenterX() {
        return cordX + width / 2;
    }

    public double getCenterY() {
        return cordY + width / 2;
    }

    public boolean isCornerValid(double newX, double newY, Map map) {
        return map.isInbounds(newX, newY) && map.getTile(newX, newY).isWalkable();

    }
}
