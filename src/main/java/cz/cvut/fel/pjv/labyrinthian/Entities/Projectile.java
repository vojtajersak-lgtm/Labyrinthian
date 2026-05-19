package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Components.Utils;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;

/**
 * Represents a projectile fired by the boss.
 * Moves in a fixed direction each frame and deactivates on player hit, wall hit,
 * or when it travels too far from the boss.
 */
public class Projectile extends GameObject {
    /** Normalized horizontal direction component. */
    private double dirX;
    /** Normalized vertical direction component. */
    private double dirY;
    private double speed;
    private double damage;
    private boolean isActive;
    /** Size of the projectile hitbox in pixels. */
    private double size;


    public Projectile(double cordX, double cordY, double dirX, double dirY,
                      double speed, double damage, boolean isActive, double size) {
        super(cordX, cordY);
        this.dirX = dirX;
        this.dirY = dirY;
        this.speed = speed;
        this.damage = damage;
        this.isActive = isActive;
        this.size = size;
    }

    public double getSize() { return size; }
    public double getDamage() { return damage; }
    public void setDamage(double damage) { this.damage = damage; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    /**
     * Moves the projectile by one frame - moves it along its direction vector
     * and checks if it hit anything.
     *
     * @param gameManager the game manager for collision and damage
     */
    public void update(GameManager gameManager) {
        // Move along direction vector
        this.cordX += dirX * speed;
        this.cordY += dirY * speed;

        boolean playerHit = (Utils.distance(getCenterX(), getCenterY(),
                gameManager.getMainCharacter().getCenterX(),
                gameManager.getMainCharacter().getCenterY()) < (size + gameManager.getMainCharacter().getWidth()));

        // Deactivate if player hit, wall hit, or traveled beyond 1500px from boss
        if (playerHit || !(isCornerValid(cordX, cordY, gameManager.getMap())) ||
                (Utils.distance(getCenterX(), getCenterY(),
                        gameManager.getBoss().getCenterX(),
                        gameManager.getBoss().getCenterY())) >= 1500) {
            isActive = false;
            if (playerHit) {
                gameManager.getMainCharacter().takeDamage(damage, gameManager);
            }
        }
    }
}
