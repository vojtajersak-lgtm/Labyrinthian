package cz.cvut.fel.pjv.labyrinthian.Components;

import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.Consumable;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final List<Item> inventoryList;
    private final int inventorySize;
    private Consumable activeItem;

    public Inventory() {
        this.activeItem = null;
        this.inventorySize = 5;
        this.inventoryList = new ArrayList<>();
    }

    public void addItem(Item item){
        inventoryList.add(item);
    }

    public void removeItem(Item item){

    }

    public boolean isFull(){
        return inventoryList.size() == inventorySize;
    }

    public Consumable getActiveItem() {
        return activeItem;
    }

    public List<Item> getInventoryList() {
        return inventoryList;
    }

    public void setActiveItem(Consumable activeItem) {
        this.activeItem = activeItem;
    }
}
