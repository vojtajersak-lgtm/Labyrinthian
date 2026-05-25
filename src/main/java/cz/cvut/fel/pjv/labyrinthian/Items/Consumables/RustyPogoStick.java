package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.World.TileType;

/**
 * Infinite-use consumable that lets the player jump over a single hedge tile.
 * The tile behind the hedge must be walkable. Tradeoff for unlimited use
 * is that unfortunately player is not very skillful and POGO stick is very old
 * - player hurts himself for 1 heart with every use
 * triggers "splat" after landing
 */
public class RustyPogoStick extends Consumable {

    private static final double POGO_DAMAGE = 2;
    private static final int POGO_RECOVERY_FRAMES = 60;
    private static final int JUMP_TILE_OFFSET = 2;

    public RustyPogoStick() {
        super("Rusty POGO stick", """
                A handy tool for jumping over pesky hedges. Seems to have rusted over the years - could hurt an unexperienced user...
                
                -allows player to jump over hedgewall
                -deals 1 heart of damage on use""", -1);
    }

    /**
     * Teleports the player two tiles forward (over a hedge) if the landing tile is walkable.
     * Applies 2 damage and a 60-frame animation timer on successful use.
     */
    @Override
    public void applyEffect(Player player, GameManager gameManager) {
        int tileX = (int) (player.getCordX() / GameManager.TILE_SIZE) + player.getDirection().dx;
        int tileY = (int) (player.getCordY() / GameManager.TILE_SIZE) + player.getDirection().dy;
        int tileXBehind = (int) (player.getCordX() / GameManager.TILE_SIZE) + JUMP_TILE_OFFSET * player.getDirection().dx;
        int tileYBehind = (int) (player.getCordY() / GameManager.TILE_SIZE) + JUMP_TILE_OFFSET * player.getDirection().dy;
        // Only jump if there's a hedge directly ahead and open ground behind it
        if (gameManager.getMap().getTileByIndex(tileX, tileY).getTile() == TileType.HEDGE
                && gameManager.getMap().getTileByIndex(tileXBehind, tileYBehind).getTile() == TileType.PATH) {
            player.setCordX(tileXBehind * GameManager.TILE_SIZE);
            player.setCordY(tileYBehind * GameManager.TILE_SIZE);
            player.takeDamage(POGO_DAMAGE, gameManager);
            player.setRecoveryCooldown(POGO_RECOVERY_FRAMES);
        }
    }

    @Override
    public int getSpriteIndex() { return 3; }
}
