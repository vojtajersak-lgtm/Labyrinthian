package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

public class BlindingStew extends Consumable {
    public BlindingStew() {
        super("Blinding Stew", "Applies a random positive effect", 6);

    }

    @Override
    public void applyEffect(Player player, GameManager gameManager) {

    }
}
