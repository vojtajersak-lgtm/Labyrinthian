package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

public class CO2Shears extends Consumable {
    public CO2Shears() {
        super("Laser sharpened shears", "Super hardened boron shears of American production, hardened with a CO2 laser. Makes a sizable hole in a hedge.", 1);
    }

    @Override
    public void use(Player player, GameManager gameManager) {

    }
}
