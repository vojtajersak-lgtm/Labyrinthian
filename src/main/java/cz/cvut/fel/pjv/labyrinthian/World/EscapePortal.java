package cz.cvut.fel.pjv.labyrinthian.World;

import cz.cvut.fel.pjv.labyrinthian.Components.Interactable;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Core.GameState;
import cz.cvut.fel.pjv.labyrinthian.Entities.GameObject;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

public class EscapePortal extends GameObject implements Interactable {
    public EscapePortal(double cordX, double cordY) {
        super(cordX, cordY);
    }

    @Override
    public void onInteraction(Player player, GameManager gameManager) {
        gameManager.setCurrentState(GameState.LEVEL_COMPLETE);
        System.out.println("level complete");
    }




}
