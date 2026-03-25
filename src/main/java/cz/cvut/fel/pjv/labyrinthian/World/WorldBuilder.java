package cz.cvut.fel.pjv.labyrinthian.World;

import cz.cvut.fel.pjv.labyrinthian.Entities.Enemy;
import cz.cvut.fel.pjv.labyrinthian.Entities.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public int[] getRandomPosition(Map map, List<? extends Entity> entities){
        int[] positionXY = new int[2];
        Random random = new Random();
        boolean foundValid = false;
        while(!foundValid) {
            boolean isOccupied = false;
            int xPos = random.nextInt(0, map.getWidth());
            int yPos = random.nextInt(0, map.getHeight());
            for(Entity e : entities){
                if(e.getCordX() == xPos && e.getCordY() == yPos){
                    isOccupied = true;
                    break;
                }
            }

            if(map.getTile(xPos,yPos).isWalkable() && !isOccupied){
                positionXY[0] = xPos;
                positionXY[1] = yPos;
                foundValid = true;

            }

        }
        return  positionXY;
    }


    public List<Enemy> buildEnemies(int count, Map map){

        List<Enemy> enemyList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int[] enemyCords = getRandomPosition(map, enemyList);
            Enemy enemy = new Enemy(enemyCords[0],enemyCords[1],6,
                    1,1,1);
            enemyList.add(enemy);
        }
        return enemyList;
    }
}
