package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Components.Inventory;
import cz.cvut.fel.pjv.labyrinthian.Components.Utils;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Core.GameState;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.Sword;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.UltimateObliterator;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.Weapon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player character.
 * Manages inventory, active weapon, attack logic and damage recovery.
 */
public class Player extends Entity {

    private static final double INITIAL_MAX_HEALTH = 6;
    private static final double CLOSE_HIT_RANGE_FACTOR = 0.75;
    private static final double CLAYPOT_HIT_RANGE = 60;

    private final Inventory inventory;
    private Weapon activeweapon;
    /** Default stat values for resetting upgrades between levels: [maxHp, speedMult, damage, range]. */
    private final double[] deafaultValues;
    /** Life steal flag - player heals for their attack damage on hit when true. Set by BlindingStew. */
    private boolean lifeStealActive = false;
    /** Frames remaining before player gets up after splatting on the ground after hurting himself trying to do cool trick on an old POGO stick... */
    private int recoveryCooldown;


    public Player(double cordX, double cordY, double height, double width, double attackRange) {
        super(cordX, cordY, height, width, INITIAL_MAX_HEALTH, attackRange);
        this.inventory = new Inventory();
        this.activeweapon = new Sword();
        this.deafaultValues = new double[]{maxHealth, 1.0, activeweapon.getDamage(), attackRange};
        this.recoveryCooldown = 0;
    }

    public void setLifeStealActive(boolean lifeStealActive) { this.lifeStealActive = lifeStealActive; }
    public boolean isLifeStealActive() { return lifeStealActive; }
    public Inventory getInventory() { return inventory; }
    public Weapon getActiveweapon() { return activeweapon; }
    public void setActiveweapon(Weapon activeweapon) { this.activeweapon = activeweapon; }
    public double[] getDeafaultValues() { return deafaultValues; }
    public int getRecoveryCooldown() { return recoveryCooldown; }
    public void setRecoveryCooldown(int recoveryCooldown) { this.recoveryCooldown = recoveryCooldown; }

    /**
     * Updates the indexed value in the deafaultValues array
     * Used to reset player values after blinding stew use
     *
     * @param value the new value
     * @param index which default to update (0=maxHp, 1=speed, 2=damage, 3=range)
     */
    public void setDeafaultValues(double value, int index) {
        this.deafaultValues[index] = value;
    }

    /**
     * Triggers game over, sets state to GAME_OVER and deletes the save file
     * so a dead player cannot reload their run.
     *
     * @param gameManager the game manager for state changes
     */
    @Override
    public void onDeath(GameManager gameManager) {
        gameManager.setCurrentState(GameState.GAME_OVER);
        LOG.info("deleting savefile");
        try {
            Files.deleteIfExists(Path.of("LabyrinthianSave.json"));
        } catch (IOException e) {
            LOG.warn("Could not delete save file: {}", e.getMessage());
        }
    }

    /**
     * Performs a melee attack in the current facing direction.
     * Hits all enemies and clay pots within the attack area or range.
     * If player is close enough (such as literally under the entity),
     * that entity is hit even if player is facing wrong direction.
     * If life steal is active (and weapon is not the UltimateObliterator), heals for attackDamage on hit.
     * Removes dead entities from their respective lists.
     *
     * @param enemyList   list of active enemies
     * @param boss        the boss entity (may be null)
     * @param pots        list of clay pots
     * @param gameManager the game manager for damage and death handling
     */
    public void attack(List<Enemy> enemyList, Boss boss, List<ClayPot> pots, GameManager gameManager) {
        // Calculate the attack hitbox based on facing direction
        double attackX = cordX + (direction.dx > 0 ? width : direction.dx < 0 ? -attackRange : 0);
        double attackY = cordY + (direction.dy > 0 ? height : direction.dy < 0 ? -attackRange : 0);
        double attackW = direction.dx != 0 ? attackRange : width;
        double attackH = direction.dy != 0 ? attackRange : height;

        List<Enemy> toRemove = new ArrayList<>();
        for (Enemy e : enemyList) {
            // hits if player is facing right direction and attack hitBox crosses enemy hitbox
            if ((attackX < e.getCordX() + e.getWidth() &&
                    attackX + attackW > e.getCordX() &&
                    attackY < e.getCordY() + e.getHeight() &&
                    attackY + attackH > e.getCordY()) ||
                    //if player is close enough, hit regardless of facing direction
                    Utils.distance(cordX, cordY, e.getCenterX(), e.getCenterY()) <= attackRange * CLOSE_HIT_RANGE_FACTOR) {
                e.takeDamage(this.activeweapon.getDamage(), gameManager);
                // Life steal: heal on hit, but not with UltimateObliterator
                if (lifeStealActive && !(activeweapon instanceof UltimateObliterator)) {
                    heal(activeweapon.getDamage(), gameManager);
                }
                if (e.isDead()) toRemove.add(e);
            }
        }
        enemyList.removeAll(toRemove);

        // Attack boss if present and not transformed - don't want to get friendly NPCs killed do we
        if (boss != null && !boss.isTransformed()) {
            if ((attackX < boss.getCordX() + boss.getWidth() &&
                    attackX + attackW > boss.getCordX() &&
                    attackY < boss.getCordY() + boss.getHeight() &&
                    attackY + attackH > boss.getCordY()) ||
                    Utils.distance(cordX, cordY, boss.getCordX(), boss.getCordY()) <= attackRange) {
                boss.takeDamage(this.activeweapon.getDamage(), gameManager);
                if (lifeStealActive && !(activeweapon instanceof UltimateObliterator)) {
                    heal(activeweapon.getDamage(), gameManager);
                }
                if (boss.isDead()) {
                    gameManager.removeBoss();
                }
            }
        }

        // Attack clay pots within range
        List<ClayPot> toRemovePots = new ArrayList<>();
        for (ClayPot c : pots) {
            if (Utils.distance(cordX, cordY, c.getCordX(), c.getCordY()) < CLAYPOT_HIT_RANGE) {
                c.takeDamage(this.activeweapon.getDamage(), gameManager);
                if (c.isDead()) toRemovePots.add(c);
            }
        }
        pots.removeAll(toRemovePots);
    }

    /**
     * Decrements the recovery cooldown by one frame.
     * Called each frame while the player is in a stunned/invincible state.
     */
    public void tickCooldown() {
        recoveryCooldown--;
    }
}
