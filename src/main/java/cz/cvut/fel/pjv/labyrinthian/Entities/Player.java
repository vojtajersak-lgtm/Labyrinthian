package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Components.Inventory;
import cz.cvut.fel.pjv.labyrinthian.Components.Utils;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Items.LooseItem;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.Sword;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity{
    private Inventory inventory;
    private Weapon activeweapon;
    private double[] deafaultValues;

    public Player(double cordX, double cordY, double height, double width, double attackRange) {
        super(cordX, cordY,height, width ,6, attackRange);
        this.inventory = new Inventory();
        this.activeweapon = new Sword();
        this.deafaultValues = new double[]{maxHealth, 1.0, activeweapon.getDamage(), attackRange};

    }


    public Inventory getInventory() {
        return inventory;
    }

    public Weapon getActiveweapon() {
        return activeweapon;
    }

    public double[] getDeafaultValues() {
        return deafaultValues;
    }

    public void setActiveweapon(Weapon activeweapon) {
        this.activeweapon = activeweapon;
    }

    public void setDeafaultValues(double value, int index) {
        this.deafaultValues[index] =  value;
    }

    @Override
    public void onDeath(GameManager gameManager) {

    }

    public void attack(List<Enemy> enemyList,List<ClayPot> Pots,GameManager gameManager){
        double attackX = cordX + (direction.dx > 0 ? width : direction.dx < 0 ? -attackRange : 0);
        double attackY = cordY + (direction.dy > 0 ? height : direction.dy < 0 ? -attackRange : 0);
        double attackW = direction.dx != 0 ? attackRange : width;
        double attackH = direction.dy != 0 ? attackRange : height;

        List<Enemy> toRemove = new ArrayList<>();
        for(Enemy e : enemyList){

            if((attackX < e.getCordX() + e.getWidth() &&
                    attackX + attackW > e.getCordX() &&
                    attackY < e.getCordY() + e.getHeight() &&
                    attackY + attackH > e.getCordY() )||
                    Utils.distance(cordX, cordY, e.getCordX(),e.getCordY()) <= attackRange){
                e.takeDamage(this.activeweapon.getDamage(), gameManager);
                if(e.isDead()) toRemove.add(e);
            }
        }
        enemyList.removeAll(toRemove);

        List<ClayPot> toRemovePots = new ArrayList<>();
        for(ClayPot c : Pots){
            if(Utils.distance(cordX, cordY, c.getCordX(),c.getCordY()) < 60){
                c.takeDamage(this.activeweapon.getDamage(), gameManager);
                if(c.isDead()) toRemovePots.add(c);
            }
        }
        Pots.removeAll(toRemovePots);
    }



}
