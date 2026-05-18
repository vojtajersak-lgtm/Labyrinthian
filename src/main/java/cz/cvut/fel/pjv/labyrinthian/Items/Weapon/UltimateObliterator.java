package cz.cvut.fel.pjv.labyrinthian.Items.Weapon;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

/**
 * A legendary weapon that kills every enemy in one hit - but permanently
 * reduces the player's max health to half a heart and cannot be unequipped.
 * High risk, high reward.
 */
public class UltimateObliterator extends Weapon {
    public UltimateObliterator() {
        super("Ultimate Obliterator", """
                A legendary weapon wielding immense power, everyone shall fall upon being hit, including you...
                -kills every enemy in one hit
                -permanently sets players health to half a heart
                -cant be deactivated""", Integer.MAX_VALUE, 1, 1);
    }

    /**
     * Equips the obliterator, caps player health at 1 and enables the obliterator flag
     * which prevents healing above 1 HP for the rest of the run.
     */
    @Override
    public void onInteraction(Player player, GameManager gameManager) {
        player.setActiveweapon(this);
        player.setMaxHealth(1);
        player.heal(1, gameManager);
        gameManager.setHasObliterator(true);
        player.setDeafaultValues(1, 0);
    }

    @Override
    public int getSpriteIndex() { return 7; }
}
