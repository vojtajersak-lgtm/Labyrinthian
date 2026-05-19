package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Components.Interactable;
import cz.cvut.fel.pjv.labyrinthian.Components.Utils;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import javafx.scene.paint.Color;

/**
 * The main boss enemy with three attack modes: AOE, melee (via Enemy AI) and ranged projectiles.
 * Can be transformed into a peaceful NPC by using a SnickersBar nearby.
 * <p>
 * Attack logic:
 * <ul>
 *   <li>Player within 150px -> triggers AOE attack</li>
 *   <li>Player within is further and boss has  LoS -> fires 5 projectiles in a cone shape</li>
 *   <li>Otherwise -> uses standard Enemy chase/melee AI</li>
 * </ul>
 */
public class Boss extends Enemy implements Interactable{
    private boolean isTransformed;
    /** Frames until the boss can fire projectiles again. */
    private int projectileCountdown;
    /** Frames remaining for the attack sprite variant to show/hide. */
    private int spriteChangeTimer;
    private boolean aoeActive = false;
    private double aoeRadius;
    private final double aoeMaxRadius;
    /** Color of the AOE ring (gray while expanding, dark red on explosion). Transient — not saved. */
    private transient Color aoeColor;
    /** Frames remaining for the explosion flash before AOE resets. */
    private int aoeFlashTimer;
    /** True once the AOE has reached max radius and exploded — prevents double-triggering. */
    private boolean aoeExploded = false;

    public Boss(double cordX, double cordY, double height, double width, double maxHealth,
                double baseDamage, int attackSpeed, double attackRange) {
        super(cordX, cordY, height, width, maxHealth, baseDamage, attackSpeed, attackRange);
        this.isTransformed = false;
        this.projectileCountdown = 60;
        this.aoeMaxRadius = 320;
        this.spriteChangeTimer = 0;
        this.aoeRadius = 0;
        this.aoeColor = Color.GRAY;
        this.aoeFlashTimer = 0;
    }

    public boolean isTransformed() { return isTransformed; }
    public void setTransformed(boolean transformed) { isTransformed = transformed; }
    public int getSpriteChangeTimer() { return spriteChangeTimer; }
    public boolean isAoeActive() { return aoeActive; }
    public double getAoeRadius() { return aoeRadius; }
    public Color getAoeColor() { return aoeColor; }
    public int getAoeFlashTimer() { return aoeFlashTimer; }

    /**
     * Spawns the escape portal and moves the boss sprite so that it does not blick the portal.
     *
     * @param gameManager the game manager
     */
    @Override
    public void onDeath(GameManager gameManager) {
        gameManager.getGamestats().addKillScore(true, false);
        gameManager.spawnPortal(getCenterX(), getCenterY());
        cordX -= 120;
    }



    /**
     * Executes one frame of boss AI.
     * <p>
     * If the player is very close, the boss activates an expanding AOE ring.
     * Otherwise it chases the player (using the parent Enemy AI) and fires projectile
     * spreads when it has line-of-sight and the cooldown has expired.
     *
     * @param player          the player target
     * @param map             the current map
     * @param gameManager     the game manager
     * @param chaseThreshold  unused (boss uses fixed distances)
     */
    @Override
    public void takeTurn(Player player, Map map, GameManager gameManager, int chaseThreshold) {
        if (isTransformed) return; // Transformed boss stands still and does nothing, renderer changes its sprite to a friendly npc

        spriteChangeTimer--;
        double distanceToPlayer = Utils.distance(player.getCordX(), player.getCordY(), getCenterX(), getCenterY());

        if (aoeActive) {
            updateAoe(player, distanceToPlayer, gameManager);
        } else {
            if (distanceToPlayer <= 150) {
                // Player too close — trigger AOE
                aoeActive = true;
            } else {
                // Chase and potentially fire projectiles
                super.takeTurn(player, map, gameManager, 10);
                if (projectileCountdown > 0) {
                    projectileCountdown--;
                } else {
                    boolean hasLoS = hasLineOfSight(player, map);
                    if (hasLoS && gameManager.getProjectiles().isEmpty()) {
                        spawnProjectiles(gameManager);
                        spriteChangeTimer = 40;
                        projectileCountdown = 0;
                    }
                }
            }
        }
    }

    /**
     * Updates AOE state each frame — expands the ring, triggers explosion at max radius,
     * then resets after the flash timer expires.
     *
     * @param player           the player for damage checking
     * @param distanceToPlayer current distance to the player
     */
    private void updateAoe(Player player, double distanceToPlayer, GameManager gameManager) {
        if (aoeRadius < aoeMaxRadius) {
            aoeRadius += 3;
            // Boss "jumps" before AOE explosion, AOE explodes upon landing, gives visual clue to player
            if (aoeRadius >= aoeMaxRadius - 80 && aoeRadius <= aoeMaxRadius - 40) cordY -= 5;
            if (aoeRadius >= aoeMaxRadius - 40) cordY += 5;
        }

        // Trigger explosion once the ring reaches max radius
        if (aoeRadius >= aoeMaxRadius && aoeFlashTimer == 0 && !aoeExploded) {
            aoeColor = Color.DARKRED;
            aoeExploded = true;
            aoeFlashTimer = 40;
            if (distanceToPlayer <= 320) player.takeDamage(baseDamage, gameManager); // damage handled before gameManager needed
        }

        // Count down flash timer, then reset AOE
        if (aoeFlashTimer > 0) {
            aoeFlashTimer--;
        } else if (aoeExploded) {
            aoeActive = false;
            aoeExploded = false;
            aoeRadius = 0;
            aoeColor = Color.GRAY;
            projectileCountdown = 0;
        }
    }

    /**
     * Fires 5 projectiles in a cone shape toward the player using rotation matrices.
     * The spread angles are {-25, -15, 0, 15, 25} degrees around the direct line to the player.
     *
     * @param gameManager the game manager to add projectiles to
     */
    public void spawnProjectiles(GameManager gameManager) {
        double[] angles = {-25, -15, 0, 15, 25};
        // Compute normalized direction vector toward player
        double dx = gameManager.getMainCharacter().getCenterX() - getCenterX();
        double dy = gameManager.getMainCharacter().getCenterY() - getCenterY();
        double len = Math.sqrt(dx * dx + dy * dy);
        dx /= len;
        dy /= len;

        for (double angle : angles) {
            // Rotate direction vector by angle using 2D rotation matrix, Linear algebra getting more useful by the day :]
            double radianAngle = Math.toRadians(angle);
            double newDx = dx * Math.cos(radianAngle) - dy * Math.sin(radianAngle);
            double newDy = dx * Math.sin(radianAngle) + dy * Math.cos(radianAngle);
            gameManager.getProjectiles().add(
                new Projectile(getCenterX(), getCenterY(), newDx, newDy, 5, baseDamage, true, 20));
        }
    }

    /**
     * Transforms the boss into a peaceful NPC.
     * Spawns the escape portal, shows the NPC dialog and moves the boss sprite aside.
     *
     * @param gameManager the game manager
     */
    public void transform(GameManager gameManager) {
        isTransformed = true;
        gameManager.spawnPortal(getCenterX(), getCenterY());
        cordX -= 100;
    }

    @Override
    public void onInteraction(Player player, GameManager gameManager) {
        gameManager.getDialogScreen().showNpcDialog();
    }
}
