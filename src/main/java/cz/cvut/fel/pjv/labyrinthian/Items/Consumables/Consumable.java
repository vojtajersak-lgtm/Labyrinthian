package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Components.Interactable;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
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

    @Override
    public void onInteraction(Player player, GameManager gameManager) {
        player.getInventory().addItem(this);
    }

    @Override
    public void use(Player player, GameManager gameManager) {
        System.out.println("item used!");
        applyEffect(player, gameManager);
        decreaseUses();
        if(usedUp()){
            gameManager.getMainCharacter().getInventory().removeFromInventory(this);
            System.out.println("Item used up!");
        }
    }
    protected abstract void applyEffect(Player player, GameManager gameManager);

}
