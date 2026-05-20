package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

/**
 * Multi-use consumable that draws a red trail tracking the player's movement through the maze.
 * First use activates the trail; second use deactivates it.
 * The trail is stored as a list of pixel coordinates in {@code GameManager}.
 */
public class YarnBall extends Consumable {
    private boolean isActive;

    public YarnBall() {
        super("Yarn Ball", """
                A ball of red yarn, could be used to keep directions in a confusing maze.
                
                -on use starts drawing line tracking players movement
                -second use deactivates""", 350);
        isActive = false;
    }



    /**
     * Toggles the yarn ball trail on or off.
     * On activation, clears any previous trail and records the starting position.
     */
    @Override
    public void applyEffect(Player player, GameManager gameManager) {
        if (gameManager.isYarnBallActive()) {
            // Second use — deactivate the trail
            gameManager.setYarnBallActive(false);
        } else {
            // First use — start fresh trail from current player position
            gameManager.getYarnBallTrail().clear();
            gameManager.setYarnBallActive(true);
            gameManager.getYarnBallTrail().add(new double[]{player.getCordX(), player.getCordY()});
        }
    }

    @Override
    public int getSpriteIndex() { return 5; }
}
