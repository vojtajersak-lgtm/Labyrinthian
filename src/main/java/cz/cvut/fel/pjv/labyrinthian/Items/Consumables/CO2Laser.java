package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.World.TileType;

public class CO2Laser extends Consumable {
    public CO2Laser() {
        super("CO2 Laser", "CO2 laser of Soviet production, excellent at making permanent holes in hedges.\n" +
                "\n-permanentaly changes hedgewall into path", 3);
    }


    @Override
    public void applyEffect(Player player, GameManager gameManager) {
        int tileX = (int) (player.getCordX() / 64) + player.getDirection().dx;
        int tileY = (int) (player.getCordY() / 64) + player.getDirection().dy;
        if (gameManager.getMap().getTileByIndex(tileX, tileY).getTile() == TileType.HEDGE) {
            gameManager.getMap().setTileByIndex(tileX, tileY, TileType.PATH);
            decreaseUses();

        }
    }


    @Override
    public int getSpriteIndex() {
        return 1;
    }
}
