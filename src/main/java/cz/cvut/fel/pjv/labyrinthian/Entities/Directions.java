package cz.cvut.fel.pjv.labyrinthian.Entities;

public enum Directions {
    NORTH(0, -1),
    SOUTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0);

    public final int dx, dy;

    Directions(int dx, int dy) {
        this.dx = dx;
        this. dy = dy;
    }
}
