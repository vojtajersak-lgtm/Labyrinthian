package cz.cvut.fel.pjv.labyrinthian.World;

public class Map {
    private Tile[][] map;
    private int mapSize;

    public Map(Tile[][] map, int mapSize) {
        this.map = map;
        this.mapSize = mapSize;
    }

    public Tile[][] getMap() {
        return map;
    }

    public Tile getTile(double xcord, double ycord) {
        return map[(int) (xcord / 64)][(int) (ycord / 64)];
    }

    public Tile getTileByIndex(int x, int y) {
        return map[x][y];

    }

    public void setTileByIndex(int x, int y, TileType tile) {
        map[x][y].setTile(tile);
    }

    public int getMapSize() {
        return mapSize;
    }

    public int getHeight() {
        return map.length;
    }

    public int getWidth() {
        return map[0].length;
    }

    public boolean isInbounds(double cordX, double cordY) {
        return cordX >= 0 && cordX < map[0].length * 64 && cordY >= 0 && cordY < map.length * 64;
    }

    public boolean isInboundsByIndex(int cordX, int cordY) {
        return cordX >= 0 && cordX < map[0].length && cordY >= 0 && cordY < map.length;
    }

    public int[][] exportMap(){
        int[][] intMap = new int[mapSize][mapSize];
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                switch (map[j][i].getTile()){
                    case PATH -> intMap[j][i] = 1;
                    case HEDGE -> intMap[j][i] = 2;
                    case ARENA_WALL -> intMap[j][i] = 3;

                }

            }

        }
        return  intMap;
    }

    public void loadMap(int[][] mapMatrix){
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                switch (mapMatrix[j][i]){
                    case 1 -> setTileByIndex(j, i, TileType.PATH);
                    case 2 -> setTileByIndex(j, i, TileType.HEDGE);
                    case 3 -> setTileByIndex(j, i, TileType.ARENA_WALL);
                    default -> setTileByIndex(j, i, TileType.PATH);

                }

            }

        }
    }
}
 