package cz.cvut.fel.pjv.labyrinthian.Entities;

public class Enemy extends Entity{
    private int baseDamage;
    private int attackSpeed;
    private int attackRange;

    public Enemy(int cordY, int cordX, int maxHealth, int baseDamage, int attackSpeed, int attackRange) {
        super(cordY, cordX, maxHealth);
        this.baseDamage = baseDamage;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;
    }
}
