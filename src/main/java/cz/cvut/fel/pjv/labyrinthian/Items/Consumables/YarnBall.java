package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

public class YarnBall extends Consumable {
    private boolean isActive;
    public YarnBall() {
        super("Yarn Ball", "A ball of red yarn, could be used to keep directions in a confusing maze.", 30);
       isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void activate(){
        isActive = true;
    }

    @Override
    public void applyEffect(Player player, GameManager gameManager) {

    }
}
