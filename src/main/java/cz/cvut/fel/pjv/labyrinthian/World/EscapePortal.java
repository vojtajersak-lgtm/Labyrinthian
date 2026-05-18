package cz.cvut.fel.pjv.labyrinthian.World;

import cz.cvut.fel.pjv.labyrinthian.Components.Interactable;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Core.GameState;
import cz.cvut.fel.pjv.labyrinthian.Entities.GameObject;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

/**
 * The escape portal - spawned in the boss arena when the boss is defeated or transformed.
 * Interacting with it triggers the level completion sequence.
 */
public class EscapePortal extends GameObject implements Interactable {

    public EscapePortal(double cordX, double cordY) {
        super(cordX, cordY);
    }

    /**
     * Transitions the game to the {@code LEVEL_COMPLETE} state,
     * triggering the next level sequence.
     *
     * @param player      the interacting player
     * @param gameManager the game manager
     */
    @Override
    public void onInteraction(Player player, GameManager gameManager) {
        gameManager.setCurrentState(GameState.LEVEL_COMPLETE);
    }
}
