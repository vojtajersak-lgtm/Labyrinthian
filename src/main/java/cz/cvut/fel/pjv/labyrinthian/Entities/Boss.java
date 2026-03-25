package cz.cvut.fel.pjv.labyrinthian.Entities;

public class Boss extends Entity{
    private int baseDamage;
    private int attackSpeed;
    private int attackRange;


    public Boss(int cordY, int cordX, int maxHealth, int baseDamage, int attackSpeed, int attackRange) {
        super(cordY, cordX, maxHealth);
        this.baseDamage = baseDamage;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;
    }
}
