package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Components.Inventory;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.Sword;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.Weapon;

public class Player extends Entity{
    private Inventory inventory;
    private Weapon activeweapon;

    public Player(int cordY, int cordX) {
        super(cordY, cordX, 6);
        this.inventory = new Inventory();
        this.activeweapon = new Sword();
    }

    @Override
    public void onDeath(GameManager gameManager) {

    }
}
