package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.World.TileType;

public class CO2Shears extends Consumable {
    public CO2Shears() {
        super("Laser sharpened shears", "Super hardened boron shears of American production, hardened with a CO2 laser. Makes a sizable hole in a hedge.", 1);
    }

    @Override
    public void applyEffect(Player player, GameManager gameManager) {
        int tileX = (int)(player.getCordX() / 64) + player.getDirection().dx;
        int tileY = (int)(player.getCordY() / 64) + player.getDirection().dy;
        if(gameManager.getMap().getTileByIndex(tileX, tileY).getTile() == TileType.HEDGE){
            gameManager.getMap().setTileByIndex(tileX,tileY, TileType.PATH);

        }
    }
}
