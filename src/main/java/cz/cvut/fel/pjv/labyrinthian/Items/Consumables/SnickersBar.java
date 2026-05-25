package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;


import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

/**
 * Single-use consumable that heals 2 hearts.
 * If used within 120px of the boss, has a chance to transform the boss into a peaceful NPC.
 * - "You're not you when you're hungry..."
 */
public class SnickersBar extends Consumable {

    private static final int HEAL_AMOUNT = 4;
    private static final int TRANSFORM_CHANCE_PERCENT = 10;
    private static final int TRANSFORM_ROLL_MAX = 100;

    public SnickersBar() {
        super("SN-1C.K.E.R.S", """
                Highly experimental chocolate bar, said to have life-changing effects on hungry individuals. Contains peanuts.
                
                -heals 2 hearts
                -10% chance to transform boss into peaceful NPC""", 1);
    }

    /**
     * Heals the player by 4 HP.
     */
    @Override
    public void applyEffect(Player player, GameManager gameManager) {
        if (!player.fullHealth()) {
            player.heal(HEAL_AMOUNT, gameManager);
            decreaseUses();
        }
    }

    /**
     * attempts to transform the boss into a friendly npc
     * 10% chance to transform, item consumed regardless of success
     */
    public void attempTransofrmation(GameManager gameManager){
        int transformChance = (int) (Math.random() * TRANSFORM_ROLL_MAX);
        if (transformChance <= TRANSFORM_CHANCE_PERCENT) {
            gameManager.getBoss().transform(gameManager);
            gameManager.getGamestats().addKillScore(true, true);
        }
        gameManager.getMainCharacter().getInventory().removeFromInventory();
    }

    @Override
    public int getSpriteIndex() { return 4; }
}
