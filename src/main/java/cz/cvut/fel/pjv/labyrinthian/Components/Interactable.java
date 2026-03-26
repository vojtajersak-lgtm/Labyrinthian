package cz.cvut.fel.pjv.labyrinthian.Components;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

public interface Interactable {
     void onInteraction(Player player, GameManager gameManager);
}
