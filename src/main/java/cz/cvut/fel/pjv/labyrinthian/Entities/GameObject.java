package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.World.Map;

/**
 * Base class for all positioned objects in the game world.
 * Stores pixel coordinates and dimensions, and provides bounds-checking helpers.
 */
public abstract class GameObject {
    /** Pixel x coordinate (top-left corner of the object). */
    protected double cordX;
    /** Pixel y coordinate (top-left corner of the object). */
    protected double cordY;
    protected double width, height;

    /**
     * Creates a game object with position only (no dimensions).
     *
     * @param cordX x coordinate in pixels
     * @param cordY y coordinate in pixels
     */
    public GameObject(double cordX, double cordY) {
        this.cordY = cordY;
        this.cordX = cordX;
    }

    /**
     * Creates a game object with position and dimensions.
     *
     * @param cordX  x coordinate in pixels
     * @param cordY  y coordinate in pixels
     * @param height height in pixels
     * @param width  width in pixels
     */
    public GameObject(double cordX, double cordY, double height, double width) {
        this.cordY = cordY;
        this.cordX = cordX;
        this.width = width;
        this.height = height;
    }

    public double getCordY() { return cordY; }
    public void setCordY(double cordY) { this.cordY = cordY; }
    public double getCordX() { return cordX; }
    public void setCordX(double cordX) { this.cordX = cordX; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    /**
     * Returns the x coordinate of the center of this object.
     *
     * @return center x in pixels
     */
    public double getCenterX() { return cordX + width / 2; }

    /**
     * Returns the y coordinate of the center of this object.
     *
     * @return center y in pixels
     */
    public double getCenterY() { return cordY + width / 2; }

    /**
     * Checks whether a given pixel position is within the map bounds and on a walkable tile.
     * Used for collision detection when moving entities.
     *
     * @param newX x position in pixels to check
     * @param newY y position in pixels to check
     * @param map  the current game map
     * @return true if the position is valid (in bounds and walkable)
     */
    public boolean isCornerValid(double newX, double newY, Map map) {
        return map.isInbounds(newX, newY) && map.getTile(newX, newY).isWalkable();
    }
}
