package cz.cvut.fel.pjv.labyrinthian.Entities;

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

    public void onDeath(){}
}
