package cz.cvut.fel.pjv.labyrinthian.World;

/**
 * A single tile on the game map. Stores the tile type and its texture variant index
 * for visual variety on path tiles.
 */
public class Tile {
    private TileType tile;
    /** Index of the texture variant (0–3 for path tiles, 4 for broken pot). */
    private int textureIndex;


    public Tile(TileType tile, int textureIndex) {
        this.tile = tile;
        this.textureIndex = textureIndex;
    }

    /**
     * Returns true if this tile can be walked on (i.e. it is a PATH tile).
     *
     * @return true if walkable
     */
    public boolean isWalkable() { return tile == TileType.PATH; }

    public TileType getTile() { return tile; }
    public void setTile(TileType tile) { this.tile = tile; }
    public int getTextureIndex() { return textureIndex; }
    public void setTextureIndex(int textureIndex) { this.textureIndex = textureIndex; }
}
