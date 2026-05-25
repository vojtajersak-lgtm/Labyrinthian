package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.World.TileType;

/**
 * Single-use consumable that permanently converts the hedge tile directly
 * in front of the player into a walkable path.
 * Inspired by a scene in The Man from U.N.C.L.E.
 */
public class CO2Shears extends Consumable {
    public CO2Shears() {
        super("CO2 Sharpened Shears", "Super hardened boron shears of American production, hardened with a CO2 laser. " +
                "Makes a sizable hole in a hedge.\n\n-permanentaly changes hedgewall into path\n-1 Use", 1);
    }

    /**
     * Cuts the hedge tile directly in front of the player into a path tile.
     * Does nothing and does not consume a use if no hedge is present.
     */
    @Override
    public void applyEffect(Player player, GameManager gameManager) {
        int tileX = (int) (player.getCordX() / GameManager.TILE_SIZE) + player.getDirection().dx;
        int tileY = (int) (player.getCordY() / GameManager.TILE_SIZE) + player.getDirection().dy;
        if (gameManager.getMap().getTileByIndex(tileX, tileY).getTile() == TileType.HEDGE) {
            gameManager.getMap().setTileByIndex(tileX, tileY, TileType.PATH);
            decreaseUses();
        }
    }

    @Override
    public int getSpriteIndex() { return 2; }
}
