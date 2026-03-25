package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

public class SnickersBar extends Consumable {
    public SnickersBar() {
        super("SN-1C.K.E.R.S", "Highly experimental chocolate bar, said to have life-changing effects on hungry individuals. Contains peanuts", 1);
    }

    @Override
    public void use(Player player, GameManager gameManager) {

    }
}
