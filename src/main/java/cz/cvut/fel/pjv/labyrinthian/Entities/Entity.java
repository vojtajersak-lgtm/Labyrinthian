package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Entity extends GameObject {
    protected int maxHealth;
    protected int currHealth;
    protected Directions direction;
    protected double attackRange;
    // Logger for entity health and movement events
    private static final Logger LOG = LoggerFactory.getLogger(Entity.class);

    public Entity(double cordX, double cordY,double height, double width, int maxHealth,double attackRange) {
        super(cordX, cordY, height, width);
        this.attackRange = attackRange;
        this.maxHealth = maxHealth;
        this.currHealth = maxHealth;
        this.direction = Directions.EAST;
    }

    public Directions getDirection() {
        return direction;
    }

    public int getCurrHealth() {
        return currHealth;
    }

    public void takeDamage(int Damage, GameManager gameManager){
        currHealth = Math.max(currHealth - Damage, 0);
        // Log damage taken and remaining health
        LOG.info("{} took {} damage, health: {}/{}", this.getClass().getSimpleName(), Damage, currHealth, maxHealth);
        if(currHealth == 0) {
            LOG.info("{} died", this.getClass().getSimpleName());
            onDeath(gameManager);
        }
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