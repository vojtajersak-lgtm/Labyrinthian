package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

public class BlindingStew extends Consumable {
    public BlindingStew() {
        super("Blinding Stew", "Applies a random positive effect", 1);

    }

    @Override
    public int getSpriteIndex() {
        return 0;
    }

    @Override
    public void applyEffect(Player player, GameManager gameManager) {
        gameManager.setBlindingStewActive(true);
        int effect = (int) (Math.random() * 4);
        switch(effect) {
            case 0 -> {
                System.out.println("player gianed 3 hearts");
                gameManager.getMainCharacter().setMaxHealth(player.getMaxHealth() + 6);
                gameManager.getMainCharacter().heal(player.getMaxHealth(),gameManager);
            }
            case 1 -> {
                System.out.println("damage doubled");
                gameManager.getMainCharacter().getActiveweapon().setDamage(gameManager.getMainCharacter().getActiveweapon().getDamage() * 2);
            }
            case 2 -> {
                System.out.println("range doubled");
                gameManager.getMainCharacter().setAttackRange(gameManager.getMainCharacter().getAttackRange() * 2);

            }
            case 3 -> {
                System.out.println("movement speed doubled");
                gameManager.setSpeedMultiplier(2.0);
            }
        }
    }


}
