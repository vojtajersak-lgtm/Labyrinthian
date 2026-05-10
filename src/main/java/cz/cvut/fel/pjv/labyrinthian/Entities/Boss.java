package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Components.Interactable;
import cz.cvut.fel.pjv.labyrinthian.Components.Utils;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import javafx.scene.paint.Color;

public class Boss extends Enemy implements Interactable {
    private boolean isTransformed;
    private int projectileCountdown;
    private int spriteChangeTimer;
    private boolean aoeActive = false;
    private double aoeRadius;
    private double aoeMaxRadius;
    private Color aoeColor;
    private int aoeFlashTimer;
    private boolean aoeExploded = false;

    public Boss(double cordX, double cordY, double height, double width, int maxHealth, int baseDamage, int attackSpeed, double attackRange, boolean isTransformed) {
        super(cordX, cordY, height, width, maxHealth, baseDamage, attackSpeed, attackRange);
        this.isTransformed = false;
        this.projectileCountdown = 60;
        this.aoeMaxRadius = 320;
        this.spriteChangeTimer = 0;
        this.aoeRadius = 0;
        this.aoeColor = Color.GRAY;
        this.aoeFlashTimer = 0;
    }

    public boolean isTransformed() {
        return isTransformed;
    }

    public void setTransformed(boolean transformed) {
        isTransformed = transformed;
    }


    public AttackTypes chooseAttack() {

        return null;
    }

    public int getProjectileCountdown() {
        return projectileCountdown;
    }

    public void setProjectileCountdown(int projectileCountdown) {
        this.projectileCountdown = projectileCountdown;
    }

    public int getSpriteChangeTimer() {
        return spriteChangeTimer;
    }

    public boolean isAoeActive() {
        return aoeActive;
    }

    public void setAoeActive(boolean aoeActive) {
        this.aoeActive = aoeActive;
    }

    public double getAoeRadius() {
        return aoeRadius;
    }

    public void setAoeRadius(double aoeRadius) {
        this.aoeRadius = aoeRadius;
    }

    public double getAoeMaxRadius() {
        return aoeMaxRadius;
    }

    public void setAoeMaxRadius(double aoeMaxRadius) {
        this.aoeMaxRadius = aoeMaxRadius;
    }

    public Color getAoeColor() {
        return aoeColor;
    }

    public void setAoeColor(Color aoeColor) {
        this.aoeColor = aoeColor;
    }

    public int getAoeFlashTimer() {
        return aoeFlashTimer;
    }

    public void setAoeFlashTimer(int aoeFlashTimer) {
        this.aoeFlashTimer = aoeFlashTimer;
    }

    @Override
    public void attack(Entity target) {
        super.attack(target);
    }

    @Override
    public void onDeath(GameManager gameManager) {
        gameManager.getGamestats().addKillScore(true, isTransformed);
        gameManager.spawnPortal(getCenterX(), getCenterY());
        cordX -= 120;

    }

    @Override
    public void onInteraction(Player player, GameManager gameManager) {

    }

    @Override
    public void takeTurn(Player player, Map map, GameManager gameManager, int chaseThreshold) {
        if (isTransformed) {
            return;
        }

        spriteChangeTimer--;
        double distanceToPlayer = Utils.distance(player.getCordX(), player.getCordY(), getCenterX(), getCenterY());
        LOG.debug("distance to player:{}", distanceToPlayer);
        if (aoeActive) {
            if (aoeRadius < aoeMaxRadius) {
                aoeRadius += 3;
                if (aoeRadius >= aoeMaxRadius - 80 && aoeRadius <= aoeMaxRadius - 40) {
                    cordY -= 5;
                }
                if (aoeRadius >= aoeMaxRadius - 40) {
                    cordY += 5;
                }
            }

            if (aoeRadius >= aoeMaxRadius && aoeFlashTimer == 0 && !aoeExploded) {
                aoeColor = Color.DARKRED;
                aoeExploded = true;
                aoeFlashTimer = 40;
                if (distanceToPlayer <= 320) player.takeDamage(baseDamage, gameManager);
            }

            if (aoeFlashTimer > 0) {
                aoeFlashTimer--;
            } else if (aoeExploded) {
                aoeActive = false;
                aoeExploded = false;
                aoeRadius = 0;
                aoeColor = Color.GRAY;
                projectileCountdown = 0;
            }
        } else {
            if (distanceToPlayer <= 150) {
                aoeActive = true;

            } else {
                super.takeTurn(player, map, gameManager, 10);
                if (projectileCountdown > 0) {
                    projectileCountdown--;

                } else {
                    boolean hasLoS = hasLineOfSight(player, map);
                    LOG.debug("hasLos is {}", hasLoS);
                    if (hasLoS && gameManager.getProjectiles().isEmpty()) {
                        spawnProjectiles(gameManager);
                        spriteChangeTimer = 40;
                        projectileCountdown = 0;
                    }
                }

            }
        }


    }

    public void spawnProjectiles(GameManager gameManager) {
        double[] angles = {-25, -15, 0, 15, 25};
        double dx = gameManager.getMainCharacter().getCenterX() - getCenterX();
        double dy = gameManager.getMainCharacter().getCenterY() - getCenterY();

        double len = Math.sqrt(dx * dx + dy * dy);
        dx /= len;
        dy /= len;

        for (double angle : angles) {
            double radianAngle = Math.toRadians(angle);
            double newDx = dx * Math.cos(radianAngle) - dy * Math.sin(radianAngle);
            double newDy = dx * Math.sin(radianAngle) + dy * Math.cos(radianAngle);
            gameManager.getProjectiles().add(new Projectile(getCenterX(), getCenterY(), newDx, newDy, 5, baseDamage, true, 20));
        }

    }

    public void transform(GameManager gameManager) {
        isTransformed = true;
        gameManager.spawnPortal(getCenterX(), getCenterY());
        gameManager.getDialogScreen().showNpcDialog();
        cordX -= 100;

    }
}
