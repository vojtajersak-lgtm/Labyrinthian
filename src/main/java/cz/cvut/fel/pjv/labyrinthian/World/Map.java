package cz.cvut.fel.pjv.labyrinthian.World;

/**
 * Represents the game map as a 2D tile grid.
 * Provides pixel-coordinate and tile-index access methods,
 * and supports export/import for the save system.
 */
public class Map {
    public static final int TILE_SIZE = 64;
    /** The tile grid — accessed as map[x][y]. */
    private Tile[][] map;
    private int mapSize;

    public Map(Tile[][] map, int mapSize) {
        this.map = map;
        this.mapSize = mapSize;
    }

    public Tile[][] getMap() { return map; }

    /**
     * Returns the tile at the given pixel coordinates.
     *
     * @param xcord x coordinate in pixels
     * @param ycord y coordinate in pixels
     * @return the tile at those coordinates
     */
    public Tile getTile(double xcord, double ycord) {
        return map[(int) (xcord / TILE_SIZE)][(int) (ycord / TILE_SIZE)];
    }

    /**
     * Returns the tile at the given tile index coordinates.
     *
     * @param x tile x index
     * @param y tile y index
     * @return the tile at index (x, y)
     */
    public Tile getTileByIndex(int x, int y) { return map[x][y]; }

    /**
     * Changes the tile type at the given tile index.
     *
     * @param x    tile x index
     * @param y    tile y index
     * @param tile the new tile type
     */
    public void setTileByIndex(int x, int y, TileType tile) { map[x][y].setTile(tile); }

    public int getMapSize() { return mapSize; }
    public int getHeight() { return map.length; }
    public int getWidth() { return map[0].length; }

    /**
     * Checks whether the given pixel coordinates are within the map bounds.
     *
     * @param cordX x in pixels
     * @param cordY y in pixels
     * @return true if within bounds
     */
    public boolean isInbounds(double cordX, double cordY) {
        return cordX >= 0 && cordX < map[0].length * TILE_SIZE && cordY >= 0 && cordY < map.length * TILE_SIZE;
    }

    /**
     * Checks whether the given tile index coordinates are within the map bounds.
     *
     * @param cordX tile x index
     * @param cordY tile y index
     * @return true if within bounds
     */
    public boolean isInboundsByIndex(int cordX, int cordY) {
        return cordX >= 0 && cordX < map[0].length && cordY >= 0 && cordY < map.length;
    }

    /**
     * Exports the map as an integer matrix for JSON.
     * Encoding: 1=PATH, 2=HEDGE, 3=ARENA_WALL.
     *
     * @return integer matrix representing the tile types
     */
    public int[][] exportMap() {
        int[][] intMap = new int[mapSize][mapSize];
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                switch (map[j][i].getTile()) {
                    case PATH       -> intMap[j][i] = 1;
                    case HEDGE      -> intMap[j][i] = 2;
                    case ARENA_WALL -> intMap[j][i] = 3;
                }
            }
        }
        return intMap;
    }

    /**
     * Loads tile types from an integer matrix (from a save file).
     * Any unrecognized value defaults to PATH.
     *
     * @param mapMatrix the integer tile matrix to load
     */
    public void loadMap(int[][] mapMatrix) {
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                switch (mapMatrix[j][i]) {
                    case 1  -> setTileByIndex(j, i, TileType.PATH);
                    case 2  -> setTileByIndex(j, i, TileType.HEDGE);
                    case 3  -> setTileByIndex(j, i, TileType.ARENA_WALL);
                    default -> setTileByIndex(j, i, TileType.PATH);
                }
            }
        }
    }
}
