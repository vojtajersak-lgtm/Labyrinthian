package cz.cvut.fel.pjv.labyrinthian.World;

import cz.cvut.fel.pjv.labyrinthian.Entities.Enemy;
import cz.cvut.fel.pjv.labyrinthian.Entities.Entity;

import java.util.*;

public class WorldBuilder {
    // Tile array convention: tiles[X][Y]

    public Map buildMap(int mapSize){
        Tile[][] tiles = new Tile[mapSize][mapSize];
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                tiles[x][y] = new Tile(TileType.HEDGE);
            }
        }
        addBossArena(tiles, mapSize);
        generateMaze(tiles, mapSize);

        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                if(x == mapSize - 1 || y == mapSize - 1 || x == 0 || y == 0){
                    tiles[x][y] = new Tile(TileType.HEDGE);
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
        stack.push(new int[]{1, 1}); // {x, y}
        while(!stack.isEmpty()){
            int[] stackTop = stack.peek();
            List<int[]> neighbours = getUnvisitedNeighbors(tiles, stackTop[0], stackTop[1], visited, mapSize);

            if(!neighbours.isEmpty()){
                Random random = new Random();
                int[] randomNeighbour = neighbours.get(random.nextInt(neighbours.size()));
                List<int[]> connectingTiles = getConnectingPath(tiles, mapSize, stackTop, randomNeighbour);
                for(int[] cord : connectingTiles){
                    if(tiles[cord[0]][cord[1]].getTile() != TileType.ARENA_WALL){
                        tiles[cord[0]][cord[1]] = new Tile(TileType.PATH);
                    }
                }
                tiles[randomNeighbour[0]][randomNeighbour[1]] = new Tile(TileType.PATH);
                visited[randomNeighbour[0]][randomNeighbour[1]] = true;
                stack.push(randomNeighbour);
            } else {
                stack.pop();
            }
        }
    }

    private List<int[]> getConnectingPath(Tile[][] tiles, int mapSize, int[] source, int[] neighbour){
        // source and neighbour are {x, y}
        int stepsX = Math.abs(neighbour[0] - source[0]);
        int stepsY = Math.abs(neighbour[1] - source[1]);
        int pathStartX = Math.min(source[0], neighbour[0]);
        int pathStartY = Math.min(source[1], neighbour[1]);
        List<int[]> connectingTiles = new ArrayList<>();

        if(stepsX == 0){ // moving along Y axis
            for (int i = 0; i < stepsY; i++) {
                connectingTiles.add(new int[]{pathStartX, pathStartY + i});
                if(isInbounds(pathStartX + 1, pathStartY, mapSize)){
                    connectingTiles.add(new int[]{pathStartX + 1, pathStartY + i});
                }
                if(isInbounds(pathStartX - 1, pathStartY, mapSize)){
                    connectingTiles.add(new int[]{pathStartX - 1, pathStartY + i});
                }
            }
        } else { // moving along X axis
            for (int i = 0; i < stepsX; i++) {
                connectingTiles.add(new int[]{pathStartX + i, pathStartY});
                if(isInbounds(pathStartX, pathStartY + 1, mapSize)){
                    connectingTiles.add(new int[]{pathStartX + i, pathStartY + 1});
                }
                if(isInbounds(pathStartX, pathStartY - 1, mapSize)){
                    connectingTiles.add(new int[]{pathStartX + i, pathStartY - 1});
                }
            }
        }
        return connectingTiles;
    }

    private void addBossArena(Tile[][] tiles, int mapSize) {
        final int radius = 10;
        final int centerX = mapSize / 2;
        final int centerY = mapSize / 2;

        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                if (Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)) <= radius + 1) {
                    tiles[x][y] = new Tile(TileType.ARENA_WALL);
                }
            }
        }
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                if (Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)) <= radius) {
                    tiles[x][y] = new Tile(TileType.PATH);
                }
            }
        }
        // Entrance on the left side of the arena
        tiles[centerX - radius - 1][centerY] = new Tile(TileType.PATH);
    }

    private List<int[]> getUnvisitedNeighbors(Tile[][] tiles, int x, int y, boolean[][] visited, int mapSize){
        List<int[]> neighboursList = new ArrayList<>();
        int[][] directions = {{-4,0},{4,0},{0,-4},{0,4}}; // {deltaX, deltaY}
        for(int[] direction : directions){
            int newCordX = x + direction[0];
            int newCordY = y + direction[1];

            if(isInbounds(newCordX, newCordY, mapSize)){
                List<int[]> connectingTiles = getConnectingPath(tiles, mapSize, new int[]{x, y}, new int[]{newCordX, newCordY});
                boolean foundArenaWall = false;
                for(int[] cord : connectingTiles){
                    if(tiles[cord[0]][cord[1]].getTile() == TileType.ARENA_WALL) foundArenaWall = true;
                }
                if(!visited[newCordX][newCordY] && !foundArenaWall){
                    neighboursList.add(new int[]{newCordX, newCordY});
                }
            }
        }
        return neighboursList;
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
            if(map.getTileByIndex(xPos, yPos).isWalkable() && !isOccupied){
                positionXY[0] = xPos;
                positionXY[1] = yPos;
                foundValid = true;
            }
        }
        return positionXY;
    }

    public List<Enemy> buildEnemies(int count, Map map){
        List<Enemy> enemyList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int[] enemyCords = getRandomPosition(map, enemyList);
            Enemy enemy = new Enemy(enemyCords[0] * 64, enemyCords[1] * 64, 48,48,6, 1, 1, 1);
            enemyList.add(enemy);
        }
        return enemyList;
    }

    public boolean isInbounds(int x, int y, int mapsize){
        return(x >= 0 && x < mapsize && y >= 0 && y < mapsize);
    }

}

