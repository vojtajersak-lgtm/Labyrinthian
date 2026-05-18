package cz.cvut.fel.pjv.labyrinthian.Items;

import cz.cvut.fel.pjv.labyrinthian.Components.Interactable;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.Weapon;

/**
 * Wrapper for an item that has been dropped on the ground.
 * Stores the item's world position and handles pickup interaction.
 */
public class LooseItem implements Interactable {
    private Item item;
    private double cordX, cordY;


    public LooseItem(Item item, double cordX, double cordY) {
        this.item = item;
        this.cordX = cordX;
        this.cordY = cordY;
    }

    public Item getItem() { return item; }
    public double getCordX() { return cordX; }
    public double getCordY() { return cordY; }

    /**
     * Picks up this item if the player's inventory has space (or if it's a weapon).
     * Removes this loose item from the world after pickup.
     *
     * @param player      the player picking up the item
     * @param gameManager the game manager for removing from the world
     */
    @Override
    public void onInteraction(Player player, GameManager gameManager) {
        // Weapons can always be picked up regardless of inventory state
        if ((!player.getInventory().inventoryFull()) || item instanceof Weapon) {
            item.onInteraction(player, gameManager);
            gameManager.getLooseItemList().remove(this);
        }
    }
}
