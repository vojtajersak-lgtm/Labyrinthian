package cz.cvut.fel.pjv.labyrinthian.World;

public class Map {
    private Tile[][] map;
    private int mapSize;

    public Map(Tile[][] map,int mapSize) {
        this.map = map;
        this.mapSize = mapSize;
    }

    public Tile[][] getMap() {
        return map;
    }

    public Tile getTile(double xcord, double ycord){
        return map[(int)(xcord/64)][(int)(ycord/64)];
    }

    public Tile getTileByIndex(int x, int y){
        return map[x][y];

    }
    public void setTileByIndex(int x, int y, TileType tile){
        map[x][y].setTile(tile);
    }

    public int getMapSize() {
        return mapSize;
    }

    public int getHeight() {
        return map.length;
    }

    public int getWidth(){
        return map[0].length;
    }

    public boolean isInbounds(double cordX, double cordY){
        return cordX >= 0 && cordX < map[0].length * 64 && cordY >= 0 && cordY < map.length *64;
    }

    public boolean isInboundsByIndex(int cordX, int cordY){
        return cordX >= 0 && cordX < map[0].length && cordY >= 0 && cordY < map.length ;
    }
}
 