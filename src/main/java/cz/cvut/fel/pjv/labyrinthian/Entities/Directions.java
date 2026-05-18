package cz.cvut.fel.pjv.labyrinthian.Entities;

/**
 * Cardinal directions used for entity movement, attacks and sprite selection.
 * Each direction stores a unit direction vector (dx, dy) and a sprite index
 * used to select player sprite with the correct facing direction
 */
public enum Directions {
    NORTH(0, -1, 0),
    SOUTH(0, 1, 1),
    EAST(-1, 0, 2),
    WEST(1, 0, 3);

    public final int dx;
    public final int dy;
    public final int index;

    Directions(int dx, int dy, int index) {
        this.dx = dx;
        this.dy = dy;
        this.index = index;
    }
}
