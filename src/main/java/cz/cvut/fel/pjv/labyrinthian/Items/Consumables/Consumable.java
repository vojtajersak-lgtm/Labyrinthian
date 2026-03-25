package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Items.Item;

public abstract class Consumable extends Item {
    private int numberOfUses;

    public Consumable(String name, String description, int numberOfUses) {
        super(name, description);
        this.numberOfUses = numberOfUses;
    }

    public void decreaseUses(){
        if(numberOfUses > 0) numberOfUses--;
    }

    public boolean usedUp(){
        return (numberOfUses == 0);
    }
}
