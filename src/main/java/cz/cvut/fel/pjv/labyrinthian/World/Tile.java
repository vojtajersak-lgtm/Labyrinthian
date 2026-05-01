package cz.cvut.fel.pjv.labyrinthian.World;

import cz.cvut.fel.pjv.labyrinthian.Entities.Entity;

public class Tile {
   private  TileType tile;
   private int textureIndex;

    public Tile(TileType tile, int textureIndex) {

        this.tile = tile;
        this.textureIndex = textureIndex;

    }

    public boolean isWalkable() {
        return tile == TileType.PATH;
    }

    public TileType getTile() {
        return tile;
    }

    public int getTextureIndex() {
        return textureIndex;
    }

    public void setTile(TileType tile) {
        this.tile = tile;
    }
}
