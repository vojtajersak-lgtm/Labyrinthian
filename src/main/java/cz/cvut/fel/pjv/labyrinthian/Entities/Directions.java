package cz.cvut.fel.pjv.labyrinthian.Entities;

public enum Directions {
    NORTH(0, -1, 0),
    SOUTH(0, 1, 1),
    EAST(-1, 0, 2),
    WEST(1, 0, 3);

    public final int dx, dy;
    public final int index;

    Directions(int dx, int dy, int index) {
        this.dx = dx;
        this.dy = dy;
        this.index = index;
    }
}
