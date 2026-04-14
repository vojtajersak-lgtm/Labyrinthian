package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;

public class ClayPot extends Entity{
    private final Item item;
    public ClayPot(double cordY, double cordX,double width, double height ,Item item) {
        super(cordY, cordX,width,height ,3);
        this.item =item;
    }

    @Override
    public void onDeath(GameManager gameManager) {
        gameManager.spawnItem(item, cordX, cordY);

    }

}
