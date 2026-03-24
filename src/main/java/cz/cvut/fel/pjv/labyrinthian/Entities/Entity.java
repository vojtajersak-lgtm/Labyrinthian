package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.World.Map;

public abstract class Entity extends GameObject {
    protected int maxHealth;
    protected int currHealth;

    public Entity(int cordY, int cordX, int maxHealth) {
        super(cordY, cordX);
        this.maxHealth = maxHealth;
        this.currHealth = maxHealth;
    }

    public int getCurrHealth() {
        return currHealth;
    }

    public void takeDamage(int Damage){
        currHealth = Math.max(currHealth - Damage, 0);
        if(currHealth == 0) onDeath();
    }

    public boolean isDead(){
        return currHealth == 0;
    }

    public void onDeath(){

    }

    public void move(int dx, int dy, Map map){
        int newCordx = cordX + dx;
        int newCordy = cordY + dy;
        if(map.isInbounds(newCordx,newCordy)  && map.getTile(newCordx, newCordy).isWalkable()){
            cordX = newCordx;
            cordY = newCordy;
        }

    }
}
