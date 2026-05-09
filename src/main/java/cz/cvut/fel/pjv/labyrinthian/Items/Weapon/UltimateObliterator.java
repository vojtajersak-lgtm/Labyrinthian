package cz.cvut.fel.pjv.labyrinthian.Items.Weapon;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

public class UltimateObliterator extends Weapon {
    public UltimateObliterator() {
        super("Ultimate Obliterator", """
                A legendary weapon wielding immense power, everyone shall fall upon being hit, including you...
                -kills every enemy in one hit
                -permanently sets players health to half a heart
                -cant be deactivated""", Integer.MAX_VALUE, 1, 1);
    }


    @Override
    public void onInteraction(Player player, GameManager gameManager) {
        player.setActiveweapon(this);
        player.setMaxHealth(1);
        player.heal(1, gameManager);
        gameManager.setHasObliterator(true);
        player.setDeafaultValues(1, 0);
    }

    @Override
    public int getSpriteIndex() {
        return 7;
    }

}
