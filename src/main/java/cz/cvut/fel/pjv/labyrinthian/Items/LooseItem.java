package cz.cvut.fel.pjv.labyrinthian.Items;

import cz.cvut.fel.pjv.labyrinthian.Components.Interactable;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.Weapon;

public class LooseItem implements Interactable {
    private Item item;
    private double cordX, cordY;

    public LooseItem(Item item, double cordX, double cordY) {
        this.item = item;
        this.cordX = cordX;
        this.cordY = cordY;
    }

    public Item getItem() {
        return item;
    }

    public double getCordX() {
        return cordX;
    }

    public double getCordY() {
        return cordY;
    }


    @Override
    public void onInteraction(Player player, GameManager gameManager) {
        if((!player.getInventory().inventoryFull()) || item instanceof Weapon){
            item.onInteraction(player, gameManager);
            gameManager.getLooseItemList().remove(this);
        }

    }
}
