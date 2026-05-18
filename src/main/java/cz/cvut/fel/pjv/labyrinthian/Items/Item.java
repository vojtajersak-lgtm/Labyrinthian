package cz.cvut.fel.pjv.labyrinthian.Items;

import cz.cvut.fel.pjv.labyrinthian.Components.Interactable;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

/**
 * Abstract base class for all items in the game - consumables and weapons.
 * Items can be picked up (via {@link Interactable#onInteraction}) and used (via {@link #use}).
 */
public abstract class Item implements Interactable {
    protected String name;
    protected String description;


    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    /**
     * Default pickup behaviour — adds this item to the player's inventory.
     * Weapons override this to equip directly instead.
     */
    @Override
    public void onInteraction(Player player, GameManager gameManager) {
        player.getInventory().addItem(this);
    }

    @Override
    public String toString() { return name; }

    /**
     * Returns the index of this item's sprite in the item sprite sheet array.
     * Must match the order defined in {@code Renderer}.
     *
     * @return sprite index
     */
    public abstract int getSpriteIndex();

    /**
     * Uses this item, applying its effect to the player and/or game state.
     *
     * @param player      the player using the item
     * @param gameManager the game manager for applying effects
     */
    public abstract void use(Player player, GameManager gameManager);
}
