package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Components.Inventory;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.Sword;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.Weapon;

import java.util.List;

public class Player extends Entity{
    private Inventory inventory;
    private Weapon activeweapon;

    public Player(double cordX, double cordY, double height, double width, double attackRange) {
        super(cordX, cordY,height, width ,6, attackRange);
        this.inventory = new Inventory();
        this.activeweapon = new Sword();

    }

    @Override
    public void onDeath(GameManager gameManager) {

    }

    public void attack(List<Enemy> enemyList,GameManager gameManager){
        double attackX = cordX + (direction.dx > 0 ? width : direction.dx < 0 ? -attackRange : 0);
        double attackY = cordY + (direction.dy > 0 ? height : direction.dy < 0 ? -attackRange : 0);
        double attackW = direction.dx != 0 ? attackRange : width;
        double attackH = direction.dy != 0 ? attackRange : height;

        for(Enemy e : enemyList){
            if(attackX < e.getCordX() + e.getWidth() &&
                    attackX + attackW > e.getCordX() &&
                    attackY < e.getCordY() + e.getHeight() &&
                    attackY + attackH > e.getCordY()){
                e.takeDamage(this.activeweapon.getDamage(), gameManager);
                System.out.println("Attacked!");
            }
        }
    }


}
