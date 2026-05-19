package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;

/**
 * A destructible clay pot placed in the maze.
 * On death, drops its contained item as a loose pickup as well as "shards" to indicate where a pot was broken to player
 * is technically entity just like enemy and boss but can't move or attack
 */
public class ClayPot extends Entity {
    /** The item dropped when this pot is destroyed. */
    private final Item item;


    public ClayPot(double cordX, double cordY, double width, double height, Item item) {
        super(cordX, cordY, width, height, 1, 0);
        this.item = item;
    }

    /**
     * Spawns the contained item as a loose pickup and updates the tile texture
     * to show a broken pot visual.
     *
     * @param gameManager the game manager for spawning the item
     */
    @Override
    public void onDeath(GameManager gameManager) {
        gameManager.spawnItem(item, getCenterX(), getCenterY());
        // Texture index 4 = broken pot variant
        gameManager.getMap().getTile(cordX, cordY).setTextureIndex(4);
    }
}
