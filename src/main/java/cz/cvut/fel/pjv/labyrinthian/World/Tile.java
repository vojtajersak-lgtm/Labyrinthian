package cz.cvut.fel.pjv.labyrinthian.World;

public class Tile {
   private  TileType tile;

    public Tile(TileType tile) {
        this.tile = tile;
    }

    public boolean isWalkable() {
        return tile == TileType.PATH;
    }
}
