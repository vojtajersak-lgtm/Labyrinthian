package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for all entities (player, enemies, boss, claypot).
 * Manages health, direction, attack range, movement and collision.
 */
public abstract class Entity extends GameObject {
    protected static final Logger LOG = LoggerFactory.getLogger(Entity.class);
    protected double maxHealth;
    protected double currHealth;
    protected Directions direction;
    protected double attackRange;


    public Entity(double cordX, double cordY, double height, double width, double maxHealth, double attackRange) {
        super(cordX, cordY, height, width);
        this.attackRange = attackRange;
        this.maxHealth = maxHealth;
        this.currHealth = maxHealth;
        this.direction = Directions.EAST;
    }

    public Directions getDirection() { return direction; }
    public void setDirection(Directions direction) { this.direction = direction; }
    public double getCurrHealth() { return currHealth; }
    public void setCurrHealth(double currHealth) { this.currHealth = currHealth; }
    public double getMaxHealth() { return maxHealth; }
    public void setMaxHealth(double maxHealth) { this.maxHealth = maxHealth; }

    /**
     * Reduces current health by the given damage amount, clamped to 0.
     * Triggers {@link #onDeath} if health reaches 0.
     *
     * @param damage      amount of damage to deal
     * @param gameManager the game manager for death handling
     */
    public void takeDamage(double damage, GameManager gameManager) {
        currHealth = Math.max(currHealth - damage, 0);
        LOG.debug("{} took {} damage, health: {}/{}", this.getClass().getSimpleName(), damage, currHealth, maxHealth);
        if (currHealth == 0) {
            LOG.debug("{} died", this.getClass().getSimpleName());
            onDeath(gameManager);
        }
    }

    /**
     * Restores health by the given amount, clamped to maxHealth.
     * If player has the Ultimate Obliterator equipped it does nothing (health is locked at 1).
     *
     * @param health      amount of health to restore
     * @param gameManager the game manager for checking the obliterator flag
     */
    public void heal(double health, GameManager gameManager) {
        if (!gameManager.isHasObliterator()) {
            currHealth = Math.min((currHealth + health), maxHealth);
            LOG.debug("{} healed {} health, health: {}/{}", this.getClass().getSimpleName(), health, currHealth, maxHealth);
        }
    }


    public boolean isDead() { return currHealth == 0; }

    public boolean fullHealth() { return currHealth == maxHealth; }

    /**
     * Called when the entity's health reaches 0.
     * Subclasses define death behavior (drop items, trigger game over, etc.).
     *
     * @param gameManager the game manager for applying death effects
     */
    public abstract void onDeath(GameManager gameManager);

    /**
     * Moves the entity by the given pixel delta, with 4-corner collision checking.
     * Only moves in a direction if all four corners of the hitbox are on walkable tiles.
     * Also updates the entity's facing direction.
     *
     * @param dx  horizontal movement in pixels
     * @param dy  vertical movement in pixels
     * @param map the current game map for collision
     */
    public void move(double dx, double dy, Map map) {
        double newCordX = cordX + dx;
        double newCordY = cordY + dy;
        // Check all four corners of the hitbox
        if (isCornerValid(newCordX, newCordY, map) &&
                isCornerValid(newCordX + width, newCordY, map) &&
                isCornerValid(newCordX, newCordY + height, map) &&
                isCornerValid(newCordX + width, newCordY + height, map)) {
            cordX = newCordX;
            cordY = newCordY;
            // Update direction
            if (Math.abs(dx) > Math.abs(dy)) {
                direction = dx > 0 ? Directions.EAST : Directions.WEST;
            } else {
                direction = dy > 0 ? Directions.SOUTH : Directions.NORTH;
            }
        }
    }
}
