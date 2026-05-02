package cz.cvut.fel.pjv.labyrinthian.Items.Consumables;

import cz.cvut.fel.pjv.labyrinthian.Components.Utils;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;

public class SnickersBar extends Consumable {
    public SnickersBar() {
        super("SN-1C.K.E.R.S", "Highly experimental chocolate bar, said to have life-changing effects on hungry individuals. Contains peanuts", 1);
    }

    @Override
    public void applyEffect(Player player, GameManager gameManager) {
        /*if(Utils.distance(player.getCordX(), player.getCordY(), gameManager.getBoss.cordX,gameManager.getBoss.cordY ) < 120){
            int transformChance = (int) (Math.random() * 100);
            if(transformChance <= 15){
                gameManager.getBoss.transform();
            }
        }else */


        if(!player.fullHealth()) player.heal(4,gameManager);
    }
}
