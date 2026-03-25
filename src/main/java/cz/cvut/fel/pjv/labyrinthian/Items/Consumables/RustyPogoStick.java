package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

public class RustyPogoStick extends Consumable {


    public RustyPogoStick(String name, String description, int numberOfUses) {
        super("Rusty POGO stick","A handy tool for jumping over pesky hedges. Seems to have rusted over the years - could hurt an unexperienced user...", -1);
    }

    @Override
    public void use(Player player, GameManager gameManager) {

    }
}
