package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Components.Utils;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

/**
 * Single-use consumable that heals 2 hearts.
 * If used within 120px of the boss, has a chance to transform the boss into a peaceful NPC.
 * - "You're not you when you're hungry..."
 */
public class SnickersBar extends Consumable {
    public SnickersBar() {
        super("SN-1C.K.E.R.S", """
                Highly experimental chocolate bar, said to have life-changing effects on hungry individuals. Contains peanuts.
                
                -heals 2 hearts
                -10% chance to transform boss into peaceful NPC""", 1);
    }

    /**
     * Heals the player by 4 HP. If the boss is nearby, attempts to transform it.
     * Transformation is not guaranteed - 10% chance to trigger.
     */
    @Override
    public void applyEffect(Player player, GameManager gameManager) {
        if (gameManager.getBoss() != null) {
            if (Utils.distance(player.getCordX(), player.getCordY(),
                    gameManager.getBoss().getCenterX(), gameManager.getBoss().getCenterY()) < 120) {
                int transformChance = (int) (Math.random() * 100);
                if (transformChance <= 10) {
                    gameManager.getBoss().transform(gameManager);
                    decreaseUses();
                }
            }
        }
        if (!player.fullHealth()) {
            player.heal(4, gameManager);
            decreaseUses();
        }
    }

    @Override
    public int getSpriteIndex() { return 4; }
}
