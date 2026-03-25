package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

public class CO2Laser extends Consumable {
    public CO2Laser() {
        super("CO2 Laser", "CO2 laser of Soviet production, excellent at making permanent holes in hedges.", 3);
    }


    @Override
    public void use(Player player, GameManager gameManager) {

    }
}
