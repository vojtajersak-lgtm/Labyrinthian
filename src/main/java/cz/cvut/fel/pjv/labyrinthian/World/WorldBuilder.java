package cz.cvut.fel.pjv.labyrinthian.World;

import cz.cvut.fel.pjv.labyrinthian.Entities.Enemy;
import cz.cvut.fel.pjv.labyrinthian.Entities.Entity;

import java.util.*;

import static java.lang.Math.random;

public class WorldBuilder {
    public Map buildMap(int mapSize){
        Tile[][] tiles = new Tile[mapSize][mapSize];
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                tiles[i][j] = new Tile(TileType.HEDGE);
            }
        }
        addBossArena(tiles, mapSize);
        generateMaze(tiles, mapSize);

        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                if(i == mapSize - 1 || j == mapSize -1){
                    tiles[i][j] = new Tile(TileType.HEDGE);
                }

            }
        }


        return new Map(tiles);

    }

    private void generateMaze(Tile[][] tiles, int mapSize) {
        Deque<int[]> stack = new ArrayDeque<>();
        boolean[][] visited = new boolean[mapSize][mapSize];
        tiles[1][1] = new Tile(TileType.PATH);
        visited[1][1] = true;
        stack.push(new int[]{1,1});
        while(!stack.isEmpty()){
           int[] stackTop = stack.peek();
            List<int[]> neighbours = getUnvisitedNeighbors(stackTop[0], stackTop[1], visited, mapSize);

           if(!neighbours.isEmpty()){
               Random random = new Random();
               int[] randomNeighbour = neighbours.get(random.nextInt(neighbours.size()));
               int middleX = (stackTop[0] + randomNeighbour[0]) / 2;
               int middleY = (stackTop[1] + randomNeighbour[1]) / 2;
               tiles[middleX][middleY] = new Tile(TileType.PATH);
               tiles[randomNeighbour[0]][randomNeighbour[1]] = new Tile(TileType.PATH);
               visited[randomNeighbour[0]][randomNeighbour[1]] = true;
               stack.push(randomNeighbour);


           }else{
               stack.pop();
           }
        }

    }

    private void addBossArena(Tile[][] tiles, int mapSize) {
       final int radius = 10;
       final int centerX = mapSize / 2;
        final int centerY = mapSize / 2;
        for (int i = 0; i < mapSize ; i++) {
            for (int j = 0; j < mapSize; j++) {
                if (Math.sqrt(Math.pow(j - centerX, 2) + Math.pow(i - centerY, 2)) <= radius + 1) {
                    tiles[i][j] = new Tile(TileType.HEDGE);
                }

            }
        }

        for (int k = 0; k < mapSize ; k++) {
            for (int l = 0; l < mapSize; l++) {
                if (Math.sqrt(Math.pow(l - centerX, 2) + Math.pow(k - centerY, 2)) <= radius) {
                    tiles[k][l] = new Tile(TileType.PATH);
                }

            }

        }
    }


    private List<int[]> getUnvisitedNeighbors(int x, int y, boolean[][] visited, int mapSize){
        List<int[]> neighboursList = new ArrayList<>();
        int[][] directions = {{0,-2},{0,2},{-2,0},{2,0}};
        for(int[] direction : directions){
            int newCordX = x + direction[0];
            int newCordY = y + direction[1];

            if(newCordX >= 0 && newCordX < mapSize
                    && newCordY >= 0 && newCordY < mapSize
                    && !visited[newCordX][newCordY])  {
                neighboursList.add(new int[]{newCordX, newCordY});
            }

        }
        return  neighboursList;
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
