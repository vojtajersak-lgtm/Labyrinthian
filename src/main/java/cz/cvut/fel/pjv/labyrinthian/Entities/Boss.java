package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Components.Interactable;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;

public class Boss extends Enemy implements Interactable {
    private  AttackTypes attackTypes;
    private boolean isTransformed;

    public Boss(double cordX, double cordY,double height, double width ,int maxHealth, int baseDamage, int attackSpeed, double attackRange, AttackTypes attackTypes, boolean isTransformed) {
        super(cordX, cordY,height, width,maxHealth, baseDamage, attackSpeed, attackRange);
        this.attackTypes = attackTypes;
        this.isTransformed = false;
    }

    public boolean isTransformed() {
        return isTransformed;
    }

    public void transform(){
        isTransformed = true;
    }

    public AttackTypes chooseAttack(){

        return null;
    }


    @Override
    public void attack(Entity target) {
        super.attack(target);
    }

    @Override
    public void onDeath(GameManager gameManager) {
        super.onDeath(gameManager);
    }

    @Override
    public void onInteraction(Player player, GameManager gameManager) {

    }
}
