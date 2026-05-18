package cz.cvut.fel.pjv.labyrinthian.World;

/**
 * The possible types of map tiles.
 */
public enum TileType {
    /** A hedge wall — impassable and blocks line of sight. */
    HEDGE,
    /** A walkable path tile. */
    PATH,
    /** The circular wall surrounding the boss arena and edges of the map. */
    ARENA_WALL
}
