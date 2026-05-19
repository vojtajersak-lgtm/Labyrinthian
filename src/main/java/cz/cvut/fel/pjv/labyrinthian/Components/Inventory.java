package cz.cvut.fel.pjv.labyrinthian.Components;

import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.*;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the player's inventory - array of 5 item slots and one additional weapon slot
 * Empty slots are represented by null. Items are placed in the first available slot.
 */
public class Inventory {
    private static final Logger LOG = LoggerFactory.getLogger(Inventory.class);
    private final Item[] inventorySlots;
    private final int inventorySize;
    private int activeIndex;

    /**
     * Creates an inventory with 5 empty slots and sets the active index to 0.
     */
    public Inventory() {
        this.inventorySize = 5;
        this.inventorySlots = new Item[5];
        this.activeIndex = 0;
        //Selection of most important items, can be uncoded to test them out
        /*inventorySlots[0] = new YarnBall();
        inventorySlots[1] = new RustyPogoStick();
        inventorySlots[2] = new CO2Laser();
        inventorySlots[3] = new BlindingStew();
        inventorySlots[4] = new SnickersBar();*/
    }

    /**
     * Adds an item to the first empty slot.
     * Does nothing if the inventory is full.
     *
     * @param item the item to add
     */
    public void addItem(Item item) {
        int i = 0;
        if (inventoryFull()) return;
        // Find the first null (empty) slot
        while (!(inventorySlots[i] == null)) {
            i++;
        }
        inventorySlots[i] = item;
        System.out.println("Added: " + item + ", inventory size: " + getSlotsFull());
    }


    public Item getActiveItem() {
        return inventorySlots[activeIndex];
    }

    public Item[] getInventorySlots() { return inventorySlots; }
    public int getActiveIndex() { return activeIndex; }

    /**
     * Sets the active inventory slot index.
     *
     * @param index slot index (0–4)
     */
    public void setActiveIndex(int index) {
        if (index < inventorySlots.length) {
            activeIndex = index;
            LOG.info("Active slot set to {}: {}", index + 1, getActiveItem());
        }
    }

    /**
     * Returns true if all 5 inventory slots are occupied.
     *
     * @return true if inventory is full
     */
    public boolean inventoryFull() {
        return getSlotsFull() == inventorySize;
    }

    /**
     * Deletes item in active slot - sets slot to null
     */
    public void removeFromInventory() {
        inventorySlots[activeIndex] = null;
    }

    /**
     * Counts the number of occupied inventory slots.
     *
     * @return number of occupied slots
     */
    public int getSlotsFull() {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            if (!(inventorySlots[i] == null)) count++;
        }
        return count;
    }
}
