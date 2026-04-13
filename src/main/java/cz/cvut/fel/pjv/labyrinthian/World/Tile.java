package cz.cvut.fel.pjv.labyrinthian.World;

import cz.cvut.fel.pjv.labyrinthian.Entities.Entity;

public class Tile {
   private  TileType tile;

    public Tile(TileType tile) {

        this.tile = tile;

    }

    public boolean isWalkable() {
        return tile == TileType.PATH;
    }

    public TileType getTile() {
        return tile;
    }
}
