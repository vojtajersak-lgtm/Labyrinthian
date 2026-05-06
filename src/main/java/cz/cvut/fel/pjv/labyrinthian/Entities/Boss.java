package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Components.Interactable;
import cz.cvut.fel.pjv.labyrinthian.Components.Utils;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import javafx.scene.paint.Color;

public class Boss extends Enemy implements Interactable {
    private boolean isTransformed;
    private int AOEcountDown;
    private boolean aoeActive = false;
    private double aoeRadius;
    private double aoeMaxRadius;
    private Color aoeColor;
    private int aoeFlashTimer;
    private boolean aoeExploded = false;

    public Boss(double cordX, double cordY,double height, double width ,int maxHealth, int baseDamage, int attackSpeed, double attackRange, boolean isTransformed) {
        super(cordX, cordY,height, width,maxHealth, baseDamage, attackSpeed, attackRange);
        this.isTransformed = false;
        this.AOEcountDown = 8;
        this.aoeMaxRadius = 320;
        this.aoeRadius = 0;
        this.aoeColor = Color.GRAY;
        this.aoeFlashTimer = 0;
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

    public int getAOEcountDown() {
        return AOEcountDown;
    }

    public void setAOEcountDown(int AOEcountDown) {
        this.AOEcountDown = AOEcountDown;
    }

    public boolean isAoeActive() {
        return aoeActive;
    }

    public void setAoeActive(boolean aoeActive) {
        this.aoeActive = aoeActive;
    }

    public double getAoeRadius() {
        return aoeRadius;
    }

    public void setAoeRadius(double aoeRadius) {
        this.aoeRadius = aoeRadius;
    }

    public double getAoeMaxRadius() {
        return aoeMaxRadius;
    }

    public void setAoeMaxRadius(double aoeMaxRadius) {
        this.aoeMaxRadius = aoeMaxRadius;
    }

    public Color getAoeColor() {
        return aoeColor;
    }

    public void setAoeColor(Color aoeColor) {
        this.aoeColor = aoeColor;
    }

    public int getAoeFlashTimer() {
        return aoeFlashTimer;
    }

    public void setAoeFlashTimer(int aoeFlashTimer) {
        this.aoeFlashTimer = aoeFlashTimer;
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


        double distanceToPlayer = Utils.distance(player.getCordX(),player.getCordY(), getCenterX(),getCenterY());

        if(aoeActive){
            if(aoeRadius < aoeMaxRadius) aoeRadius += 2;

            if(aoeRadius >= aoeMaxRadius && aoeFlashTimer == 0 && !aoeExploded){
                aoeColor = Color.DARKRED;
                aoeExploded = true;
                if(distanceToPlayer <= 320) player.takeDamage(baseDamage, gameManager);
                aoeFlashTimer = 120;
            }

            if(aoeFlashTimer > 0){
                aoeFlashTimer--;
            } else if(aoeExploded) {
                aoeActive = false;
                aoeExploded = false;
                aoeRadius = 0;
                aoeColor = Color.GRAY;
            }
        }else{
            if(distanceToPlayer <= 200){
                aoeActive = true;

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
}
