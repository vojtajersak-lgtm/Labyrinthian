package cz.cvut.fel.pjv.labyrinthian.Items;
import cz.cvut.fel.pjv.labyrinthian.Components.Interactable;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

public abstract class Item implements Interactable {
    private final String name;
    private final String description;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void onInteraction(Player player, GameManager gameManager) {
        player.getInventory().addItem(this);
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                '}';
    }

    public abstract int getSpriteIndex();

    public abstract void use(Player player, GameManager gameManager);
}
