package cz.cvut.fel.pjv.labyrinthian.Items.Weapon;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Entity;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

public class Sword extends Weapon{
    public Sword() {
        super("Sword","You should not be reading this, but if you are, congrats, i messed up", 1,1, 1);
    }

    @Override
    public void use(Player player, GameManager gameManager) {

    }


}
