package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;

/**
 * Abstract base class for all consumable items (single-use or multi-use).
 * Implements the template method pattern: {@link #use} calls {@link #applyEffect}
 * and then removes the item from inventory if all uses are spent.
 */
public abstract class Consumable extends Item {
    private int numberOfUses;

    public Consumable(String name, String description, int numberOfUses) {
        super(name, description);
        this.numberOfUses = numberOfUses;
    }

    /**
     * Decrements the use counter by one if uses remain.
     */
    public void decreaseUses() {
        if (numberOfUses > 0) numberOfUses--;
    }

    public boolean usedUp() { return (numberOfUses == 0); }


    /**
     * Applies the item's effect and removes it from inventory if all uses are spent.
     *
     * @param player      the player using the item
     * @param gameManager the game manager
     */
    @Override
    public void use(Player player, GameManager gameManager) {
        applyEffect(player, gameManager);
        if (usedUp()) {
            gameManager.getMainCharacter().getInventory().removeFromInventory();
        }
    }

    /**
     * Applies this item's specific effect to the player or game state.
     * Implemented by each concrete consumable subclass.
     *
     * @param player      the player using the item
     * @param gameManager the game manager for accessing game state
     */
    protected abstract void applyEffect(Player player, GameManager gameManager);
}
