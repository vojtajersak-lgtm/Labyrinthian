package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Components.Utils;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;


public class Projectile extends GameObject {
    private  double dirX;
    private  double dirY;
    private double speed;
    private double damage;
    private boolean isActive;
    private double size;

    public Projectile(double cordX, double cordY, double dirX, double dirY, double speed, double damage, boolean isActive, double size) {
        super(cordX, cordY);
        this.dirX = dirX;
        this.dirY = dirY;
        this.speed = speed;
        this.damage = damage;
        this.isActive = isActive;
        this.size = size;
    }

    public double getSize() {
        return size;
    }

    public double getDirX() {
        return dirX;
    }

    public void setDirX(double dirX) {
        this.dirX = dirX;
    }

    public double getDirY() {
        return dirY;
    }

    public void setDirY(double dirY) {
        this.dirY = dirY;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    public void update(GameManager gameManager){
        this.cordX += dirX * speed;
        this.cordY += dirY * speed;
        boolean playerHit = (Utils.distance(getCenterX(), getCenterY(),
                gameManager.getMainCharacter().getCenterX(), gameManager.getMainCharacter().getCenterY()) <
                (size + gameManager.getMainCharacter().getWidth()));


        if(playerHit || !(isCornerValid(cordX, cordY, gameManager.getMap())) ||
                (Utils.distance(getCenterX(), getCenterY(),
                gameManager.getBoss().getCenterX(),
                        gameManager.getBoss().getCenterY())) >= 1500){
            isActive = false;
            if(playerHit){
                gameManager.getMainCharacter().takeDamage(damage, gameManager);
            }

        }

    }
}
