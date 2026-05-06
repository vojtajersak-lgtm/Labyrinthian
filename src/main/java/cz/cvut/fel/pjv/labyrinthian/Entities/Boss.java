package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Components.Interactable;
import cz.cvut.fel.pjv.labyrinthian.Components.Utils;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.World.Map;

public class Boss extends Enemy implements Interactable {
    private  AttackTypes attackTypes;
    private boolean isTransformed;
    private int AOEcountDown;

    public Boss(double cordX, double cordY,double height, double width ,int maxHealth, int baseDamage, int attackSpeed, double attackRange, AttackTypes attackTypes, boolean isTransformed) {
        super(cordX, cordY,height, width,maxHealth, baseDamage, attackSpeed, attackRange);
        this.attackTypes = attackTypes;
        this.isTransformed = false;
        this.AOEcountDown = 8;
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

    @Override
    public void takeTurn(Player player, Map map, GameManager gameManager) {
        if(isTransformed) {
            //transform
            return;
        }


        double distanceToPlayer = Utils.distance(player.getCordX(),player.getCordY(), cordX,cordY);

        if(distanceToPlayer <= 200){
            //AOE
        } else if (distanceToPlayer > 200 &&  distanceToPlayer < 700) {
            if(AOEcountDown == 5){
                //AOE
                AOEcountDown = 0;
            }
            super.takeTurn(player, map, gameManager);
            AOEcountDown++;


        }else{
            //ranged

        }


    }
}
