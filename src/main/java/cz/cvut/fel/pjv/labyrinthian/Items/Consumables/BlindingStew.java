package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

/**
 * Single-use consumable that applies a random positive effect but blinds the player
 * by shrinking their visible area to a small radius (rendered as a dark overlay).
 * Effects (positive and negative) disappear at the end of round
 * <p>
 * Possible effects (chosen randomly on use):
 * <ul>
 *   <li>0 — adds 3 hearts</li>
 *   <li>1 — doubles weapon damage</li>
 *   <li>2 — activates life steal</li>
 *   <li>3 — doubles movement speed</li>
 * </ul>
 */
public class BlindingStew extends Consumable {
    public BlindingStew() {
        super("Blinding Stew", """
                Applies a random positive effect and shortens vision:\s
                
                -adds 3 hearts
                -activates lifesteal
                -doubles damage
                doubles movement speed!""", 1);
    }

    @Override
    public int getSpriteIndex() { return 0; }

    /**
     * Activates the blinding effect and applies one of four random buffs.
     * Updates the item's description to reflect the chosen effect.
     */
    @Override
    public void applyEffect(Player player, GameManager gameManager) {
        gameManager.setBlindingStewActive(true);
        int effect = (int) (Math.random() * 4);
        name = "Effect:";
        switch (effect) {
            case 0 -> {
                gameManager.getMainCharacter().setMaxHealth(player.getMaxHealth() + 6);
                gameManager.getMainCharacter().heal(player.getMaxHealth(), gameManager);
                description = "You gained 3 hearts!";
            }
            case 1 -> {
                gameManager.getMainCharacter().getActiveweapon().setDamage(
                        gameManager.getMainCharacter().getActiveweapon().getDamage() * 2);
                description = "Damage doubled";
            }
            case 2 -> {
                /** After successful hit, player heals for their attack damage, does not work with ultimate obliterator */
                gameManager.getMainCharacter().setLifeStealActive(true);
                description = "Life steal activated";
            }
            case 3 -> {
                gameManager.setSpeedMultiplier(2.0);
                description = "Movement speed doubled";
            }
        }
        decreaseUses();
    }
}
