package cz.cvut.fel.pjv.labyrinthian.Items.Weapon;

public class Sword extends Weapon {
    public Sword() {
        super("Sword", "You should not be reading this, but if you are, congrats, i messed up", 1, 1, 1);
    }


    @Override
    public int getSpriteIndex() {
        return 6;
    }


}
