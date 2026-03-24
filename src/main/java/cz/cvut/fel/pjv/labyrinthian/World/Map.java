package cz.cvut.fel.pjv.labyrinthian.World;

public class Map {
    private Tile[][] map;

    public Map(Tile[][] map) {
        this.map = map;
    }

    public Tile[][] getMap() {
        return map;
    }

    public Tile getTile(int xcord, int ycord){
        return map[xcord][ycord];
    }

    public int getHeight() {
        return map.length;
    }

    public int getWidth(){
        return map[0].length;
    }

    public boolean isInbounds(int cordx, int cordy){
        return cordx >= 0 && cordx < map[0].length && cordy >= 0 && cordy < map.length;
    }
}
