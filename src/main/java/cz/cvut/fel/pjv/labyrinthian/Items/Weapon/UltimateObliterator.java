package cz.cvut.fel.pjv.labyrinthian.Items.Weapon;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Entity;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

public class UltimateObliterator extends Weapon {
    public UltimateObliterator() {
        super("Ultimate Obliterator", "A legendary weapon wielding immense power, everyone shall fall upon being hit, including you...", -1, 1, 1);
    }

    @Override
    public void use(Player player, GameManager gameManager) {

    }

    @Override
    public void onInteraction(Player player, GameManager gameManager) {

    }
}
