package cz.cvut.fel.pjv.labyrinthian.Components;

import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.CO2Laser;
import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.CO2Shears;
import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.Consumable;
import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.SnickersBar;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Inventory {
    private final Item[] inventorySlots;
    private final int inventorySize;
    private int activeIndex;
    private static final Logger LOG = LoggerFactory.getLogger(Inventory.class);

    public Inventory() {
        this.inventorySize = 5;
        this.inventorySlots = new Item[5];
        this.activeIndex = 0;
        inventorySlots[0] = new SnickersBar();
        inventorySlots[1] = new SnickersBar();
        inventorySlots[2] = new CO2Laser();
        inventorySlots[3] = new CO2Shears();


    }

    public void addItem(Item item){
        int i = 0;
        while (!(inventorySlots[i] == null)){
            i++;
        }
        inventorySlots[i] = item;
        System.out.println("Added: " + item + ", inventory size: " + getSlotsFull());
    }

    public void removeItem(Item item){

    }


    public Item getActiveItem() {
        return inventorySlots[activeIndex];
    }

    public Item[] getInventorySlots() {
        return inventorySlots;
    }

    public void setActiveIndex(int index) {
        if(index < inventorySlots.length) {
            activeIndex = index;
            LOG.info("Active slot set to {}: {}", index + 1, getActiveItem());
        }

    }

    public boolean inventoryFull(){
        return getSlotsFull() == inventorySize;
    }
    public void removeFromInventory(Item item){
        inventorySlots[activeIndex] = null;
    }
    public int getSlotsFull(){
        int count = 0;
        for (int i = 0; i < 5; i++) {
            if(!(inventorySlots[i] == null)){
                count++;
            }
        }
        return  count;
    }
}
