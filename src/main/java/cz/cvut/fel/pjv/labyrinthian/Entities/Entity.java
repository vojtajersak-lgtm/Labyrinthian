package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Entity extends GameObject {
    protected double maxHealth;
    protected double currHealth;
    protected Directions direction;
    protected double attackRange;
    // Logger for entity health and movement events
    private static final Logger LOG = LoggerFactory.getLogger(Entity.class);

    public Entity(double cordX, double cordY,double height, double width, double maxHealth,double attackRange) {
        super(cordX, cordY, height, width);
        this.attackRange = attackRange;
        this.maxHealth = maxHealth;
        this.currHealth = maxHealth;
        this.direction = Directions.EAST;
    }

    public Directions getDirection() {
        return direction;
    }

    public double getCurrHealth() {
        return currHealth;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(double attackRange) {
        this.attackRange = attackRange;
    }

    public void takeDamage(double Damage, GameManager gameManager){
        currHealth = Math.max(currHealth - Damage, 0);
        LOG.info("{} took {} damage, health: {}/{}", this.getClass().getSimpleName(), Damage, currHealth, maxHealth);
        if(currHealth == 0) {
            LOG.info("{} died", this.getClass().getSimpleName());
            onDeath(gameManager);
        }
    }

    public void heal(double health, GameManager gameManager){
        currHealth = Math.min((currHealth + health), maxHealth);
        LOG.info("{} helaed {} health, health: {}/{}", this.getClass().getSimpleName(), health, currHealth, maxHealth);


    }

    public boolean isDead(){
        return currHealth == 0;
    }
    public boolean fullHealth() {return currHealth == maxHealth;}

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
            if (Math.abs(dx) > Math.abs(dy)) {
                direction = dx > 0 ? Directions.EAST : Directions.WEST;
            } else {
                direction = dy> 0 ? Directions.SOUTH : Directions.NORTH;
            }
        }

    }

    public boolean isCornerValid(double newX, double newY, Map map){
        return map.isInbounds(newX, newY) && map.getTile(newX, newY).isWalkable();

    }


}