package cz.cvut.fel.pjv.labyrinthian.Components;

import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.*;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.Weapon;
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
        inventorySlots[1] = new RustyPogoStick();
        inventorySlots[2] = new CO2Laser();
        inventorySlots[3] = new BlindingStew();
        inventorySlots[4] = new YarnBall();


    }

    public void addItem(Item item){

        int i = 0;
        if(inventoryFull()) return;
        while (!(inventorySlots[i] == null)){
            i++;
        }
        inventorySlots[i] = item;
        System.out.println("Added: " + item + ", inventory size: " + getSlotsFull());
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

    public int getActiveIndex() {
        return activeIndex;
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
