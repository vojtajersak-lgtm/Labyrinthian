package cz.cvut.fel.pjv.labyrinthian.World;

public class WorldBuilder {
    public Map buildMap(int mapSize){
        Tile[][] tiles = new Tile[mapSize][mapSize];

        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                if(i == 0 || i == mapSize - 1 || j == 0 || j == mapSize -1 || i == mapSize / 2){
                    tiles[i][j] =  new Tile(TileType.HEDGE);
                }else {
                    tiles[i][j] = new Tile(TileType.PATH);
                }
            }

        }
        return new Map(tiles);

    }
}
