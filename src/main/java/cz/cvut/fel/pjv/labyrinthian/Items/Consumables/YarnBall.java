package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

public class YarnBall extends Consumable {
    private boolean isActive;

    public YarnBall() {
        super("Yarn Ball", """
                A ball of red yarn, could be used to keep directions in a confusing maze.
                
                -on use starts drawing line tracking players movement
                -second use deactivates""", 240);
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void activate() {
        isActive = true;
    }

    @Override
    public void applyEffect(Player player, GameManager gameManager) {
        if (gameManager.isYarnBallActive()) {
            gameManager.setYarnBallActive(false);

        } else {
            gameManager.getYarnBallTrail().clear();
            gameManager.setYarnBallActive(true);
            gameManager.getYarnBallTrail().add(new double[]{player.getCordX(), player.getCordY()});

        }
    }


    @Override
    public int getSpriteIndex() {
        return 5;
    }
}
