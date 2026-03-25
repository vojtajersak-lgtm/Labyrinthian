package cz.cvut.fel.pjv.labyrinthian.Entities;

public class Boss extends Enemy{
    private  AttackTypes attackTypes;
    private boolean isTransformed;

    public Boss(int cordY, int cordX, int maxHealth, int baseDamage, int attackSpeed, int attackRange, AttackTypes attackTypes, boolean isTransformed) {
        super(cordY, cordX, maxHealth, baseDamage, attackSpeed, attackRange);
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


}
