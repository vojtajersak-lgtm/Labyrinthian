package cz.cvut.fel.pjv.labyrinthian.Items.Weapon;

/**
 * The player's default starting weapon — a basic sword with low damage and range.
 * Stats are upgraded by the player through the kill reward dialog.
 * cannot be found as loose loot or regained after player switches to ultimate obliterator
 */
public class Sword extends Weapon {
    public Sword() {
        super("Sword", "You should not be reading this, but if you are, congrats, i messed up", 1, 1, 1);
    }

    @Override
    public int getSpriteIndex() { return 6; }
}
