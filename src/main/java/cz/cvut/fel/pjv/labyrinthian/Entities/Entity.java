package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.World.Map;

public abstract class Entity extends GameObject {
    protected int maxHealth;
    protected int currHealth;

    public Entity(double cordY, double cordX,double width, double height, int maxHealth) {
        super(cordY, cordX, width, height);
        this.maxHealth = maxHealth;
        this.currHealth = maxHealth;
    }

    public int getCurrHealth() {
        return currHealth;
    }

    public void takeDamage(int Damage, GameManager gameManager){
        currHealth = Math.max(currHealth - Damage, 0);
        if(currHealth == 0) onDeath(gameManager);
    }

    public boolean isDead(){
        return currHealth == 0;
    }

    public abstract void onDeath(GameManager gameManager);

    public void move(double dx, double dy, Map map){
        double newCordx = cordX + dx;
        double newCordy = cordY + dy;
        if(isCornerValid(newCordx, newCordy,map) &&
                isCornerValid(newCordx + width, newCordy,map) &&
                isCornerValid(newCordx, newCordy + height,map) &&
                isCornerValid(newCordx + width, newCordy + height,map)){

            cordX = newCordx;
            cordY = newCordy;
        }

    }

    public boolean isCornerValid(double newX, double newY, Map map){
        return map.isInbounds(newX, newY) && map.getTile(newX, newY).isWalkable();

    }
}
