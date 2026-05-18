package cz.cvut.fel.pjv.labyrinthian.Components;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

/**
 * Interface for any game object the player can interact with by pressing E.
 * Implemented by items, loose items and the escape portal.
 */
public interface Interactable {
    /**
     * Called when the player interacts with this object.
     *
     * @param player      the player initiating the interaction
     * @param gameManager the game manager for accessing game state
     */
    void onInteraction(Player player, GameManager gameManager);
}
