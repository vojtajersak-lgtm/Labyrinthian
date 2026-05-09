package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.World.TileType;

public class RustyPogoStick extends Consumable {


    public RustyPogoStick() {
        super("Rusty POGO stick", """
                A handy tool for jumping over pesky hedges. Seems to have rusted over the years - could hurt an unexperienced user...
                
                -allows player to jump over hedgewall
                -deals 1 heart of damage on use""", -1);
    }

    @Override
    public void applyEffect(Player player, GameManager gameManager) {
        int tileX = (int) (player.getCordX() / 64) + player.getDirection().dx;
        int tileY = (int) (player.getCordY() / 64) + player.getDirection().dy;
        int tileXBehind = (int) (player.getCordX() / 64) + 2 * player.getDirection().dx;
        int tileYBehind = (int) (player.getCordY() / 64) + 2 * player.getDirection().dy;
        if (gameManager.getMap().getTileByIndex(tileX, tileY).getTile() == TileType.HEDGE
                && gameManager.getMap().getTileByIndex(tileXBehind, tileYBehind).getTile() == TileType.PATH) {
            player.setCordX(tileXBehind * 64);
            player.setCordY(tileYBehind * 64);
            player.takeDamage(2, gameManager);

        }
    }


    @Override
    public int getSpriteIndex() {
        return 3;
    }
}
