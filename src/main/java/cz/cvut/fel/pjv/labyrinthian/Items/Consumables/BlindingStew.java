package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

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
    public int getSpriteIndex() {
        return 0;
    }

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
                gameManager.getMainCharacter().getActiveweapon().setDamage(gameManager.getMainCharacter().getActiveweapon().getDamage() * 2);
                description = "Damage doubled";
            }
            case 2 -> {
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
