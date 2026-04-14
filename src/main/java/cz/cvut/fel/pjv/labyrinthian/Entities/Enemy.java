package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.World.Map;

public class Enemy extends Entity{
    private int baseDamage;
    private int attackSpeed;
    private int attackRange;
    private EnemyState state;
    private double startX, startY;       // starting position of the enemy

    public Enemy(double cordY, double cordX,double width, double height ,int maxHealth, int baseDamage, int attackSpeed, int attackRange) {
        super(cordY, cordX,width,height,maxHealth);
        this.baseDamage = baseDamage;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;
        this.state = EnemyState.IDLE;
        this.startX = cordX;
        this.startY = cordY;
    }

    @Override
    public void onDeath(GameManager gameManager) {

    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void attack(Entity target){}

    public void takeTurn(Player player, Map map){


    }


}
