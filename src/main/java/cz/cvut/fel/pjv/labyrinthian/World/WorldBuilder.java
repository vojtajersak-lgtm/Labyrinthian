package cz.cvut.fel.pjv.labyrinthian.World;

import cz.cvut.fel.pjv.labyrinthian.Entities.Boss;
import cz.cvut.fel.pjv.labyrinthian.Entities.ClayPot;
import cz.cvut.fel.pjv.labyrinthian.Entities.Enemy;
import cz.cvut.fel.pjv.labyrinthian.Entities.Entity;
import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.*;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.UltimateObliterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class WorldBuilder {
    // Tile array convention: tiles[X][Y]
    // Logger for map generation events
    private static final Logger LOG = LoggerFactory.getLogger(WorldBuilder.class);

    public Map buildMap(int mapSize) {
        LOG.info("Starting map generation, size: {}x{}", mapSize, mapSize);
        if (mapSize < 22) throw new IllegalArgumentException("Map size must be at least 22");
        Tile[][] tiles = new Tile[mapSize][mapSize];
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                tiles[x][y] = new Tile(TileType.HEDGE, 1);
            }
        }
        addBossArena(tiles, mapSize);
        generateMaze(tiles, mapSize);

        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                if (x == mapSize - 1 || y == mapSize - 1 || x == 0 || y == 0) {
                    tiles[x][y] = new Tile(TileType.ARENA_WALL, 1);
                }
            }
        }

        return new Map(tiles, mapSize);
    }

    private void generateMaze(Tile[][] tiles, int mapSize) {
        LOG.debug("Generating maze with recursive backtracker algorithm");
        Deque<int[]> stack = new ArrayDeque<>();
        boolean[][] visited = new boolean[mapSize][mapSize];
        tiles[1][1] = new Tile(TileType.PATH, getRandomTextureInt());
        visited[1][1] = true;
        stack.push(new int[]{1, 1}); // {x, y}
        while (!stack.isEmpty()) {
            int[] stackTop = stack.peek();
            List<int[]> neighbours = getUnvisitedNeighbors(tiles, stackTop[0], stackTop[1], visited, mapSize);

            if (!neighbours.isEmpty()) {
                Random random = new Random();
                int[] randomNeighbour = neighbours.get(random.nextInt(neighbours.size()));
                List<int[]> connectingTiles = getConnectingPath(tiles, mapSize, stackTop, randomNeighbour);
                for (int[] cord : connectingTiles) {
                    if (tiles[cord[0]][cord[1]].getTile() != TileType.ARENA_WALL) {
                        tiles[cord[0]][cord[1]] = new Tile(TileType.PATH, getRandomTextureInt());
                    }
                }
                tiles[randomNeighbour[0]][randomNeighbour[1]] = new Tile(TileType.PATH, getRandomTextureInt());
                visited[randomNeighbour[0]][randomNeighbour[1]] = true;
                stack.push(randomNeighbour);
            } else {
                stack.pop();
            }
        }
    }


    private void addBossArena(Tile[][] tiles, int mapSize) {
        final int radius = 10;
        final int centerX = mapSize / 2;
        final int centerY = mapSize / 2;

        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                if (Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)) <= radius + 1) {
                    tiles[x][y] = new Tile(TileType.ARENA_WALL, 1);
                }
            }
        }
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                if (Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)) <= radius) {
                    tiles[x][y] = new Tile(TileType.PATH, getRandomTextureInt());
                }
            }
        }
        // Entrance on the left side of the arena
        tiles[centerX - radius - 1][centerY] = new Tile(TileType.PATH, getRandomTextureInt());
    }

    public List<Enemy> buildEnemies(int count, Map map, double scale) {
        LOG.info("Spawning {} enemies", count);
        List<Enemy> enemyList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int[] enemyCords = getRandomPosition(map, enemyList);
            Enemy enemy = new Enemy(enemyCords[0] * 64, enemyCords[1] * 64, 48, 48, 6 * scale, 2 * scale, 3, 80);
            LOG.debug("Enemy {} spawned at ({}, {})", i + 1, enemyCords[0], enemyCords[1]);
            enemyList.add(enemy);
        }
        return enemyList;
    }

    public Boss spawnBoss(Map map, double scale) {
        Boss boss = new Boss(map.getWidth() * 32, map.getHeight() * 32, 196, 196, 20, 3, 2, 0, false);
        return boss;
    }


    public List<ClayPot> buildClaypots(int count, Map map) {
        List<ClayPot> clayPots = new ArrayList<>();
        List<int[]> deadEnds = findDeadEnds(map);
        Random random = new Random();


        for (int i = 0; i < count; i++) {
            int randomPosition = random.nextInt(deadEnds.size());
            int[] potCords = deadEnds.get(randomPosition);
            ClayPot clayPot = new ClayPot(potCords[0] * 64, potCords[1] * 64, 48, 48, getRandomItem());
            deadEnds.remove(randomPosition);
            clayPots.add(clayPot);
        }
        return clayPots;
    }

    public EscapePortal buildPortal(double cordX, double cordY) {
        return new EscapePortal(cordX, cordY);
    }


    //helper methods
    private List<int[]> getConnectingPath(Tile[][] tiles, int mapSize, int[] source, int[] neighbour) {
        // source and neighbour are {x, y}
        int stepsX = Math.abs(neighbour[0] - source[0]);
        int stepsY = Math.abs(neighbour[1] - source[1]);
        int pathStartX = Math.min(source[0], neighbour[0]);
        int pathStartY = Math.min(source[1], neighbour[1]);
        List<int[]> connectingTiles = new ArrayList<>();

        if (stepsX == 0) { // moving along Y axis
            for (int i = 0; i < stepsY; i++) {
                connectingTiles.add(new int[]{pathStartX, pathStartY + i});
                if (isInbounds(pathStartX + 1, pathStartY, mapSize)) {
                    connectingTiles.add(new int[]{pathStartX + 1, pathStartY + i});
                }
                if (isInbounds(pathStartX - 1, pathStartY, mapSize)) {
                    connectingTiles.add(new int[]{pathStartX - 1, pathStartY + i});
                }
            }
        } else { // moving along X axis
            for (int i = 0; i < stepsX; i++) {
                connectingTiles.add(new int[]{pathStartX + i, pathStartY});
                if (isInbounds(pathStartX, pathStartY + 1, mapSize)) {
                    connectingTiles.add(new int[]{pathStartX + i, pathStartY + 1});
                }
                if (isInbounds(pathStartX, pathStartY - 1, mapSize)) {
                    connectingTiles.add(new int[]{pathStartX + i, pathStartY - 1});
                }
            }
        }
        return connectingTiles;
    }

    private List<int[]> getUnvisitedNeighbors(Tile[][] tiles, int x, int y, boolean[][] visited, int mapSize) {
        List<int[]> neighboursList = new ArrayList<>();
        int[][] directions = {{-4, 0}, {4, 0}, {0, -4}, {0, 4}}; // {deltaX, deltaY}
        for (int[] direction : directions) {
            int newCordX = x + direction[0];
            int newCordY = y + direction[1];

            if (isInbounds(newCordX, newCordY, mapSize)) {
                List<int[]> connectingTiles = getConnectingPath(tiles, mapSize, new int[]{x, y}, new int[]{newCordX, newCordY});
                boolean foundArenaWall = false;
                for (int[] cord : connectingTiles) {
                    if (tiles[cord[0]][cord[1]].getTile() == TileType.ARENA_WALL) foundArenaWall = true;
                }
                if (!visited[newCordX][newCordY] && !foundArenaWall) {
                    neighboursList.add(new int[]{newCordX, newCordY});
                }
            }
        }
        return neighboursList;
    }

    public int[] getRandomPosition(Map map, List<? extends Entity> entities) {
        int[] positionXY = new int[2];
        Random random = new Random();
        boolean foundValid = false;
        while (!foundValid) {
            boolean isOccupied = false;
            int xPos = random.nextInt(0, map.getWidth());
            int yPos = random.nextInt(0, map.getHeight());
            for (Entity e : entities) {
                if (e.getCordX() == xPos && e.getCordY() == yPos) {
                    isOccupied = true;
                    break;
                }
            }
            if (map.getTileByIndex(xPos, yPos).isWalkable() && !isOccupied) {
                positionXY[0] = xPos;
                positionXY[1] = yPos;
                foundValid = true;
            }
        }
        return positionXY;
    }

    private List<int[]> findDeadEnds(Map map){
        List<int[]> deadEnds = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int i = 0; i < map.getMapSize(); i++) {
            for (int j = 0; j < map.getMapSize(); j++) {
                if(!map.getTileByIndex(j, i).isWalkable()) continue;
                else{
                    int directionsBlocked = 0;
                    for(int[] d : directions){
                        if(map.getTileByIndex(j + d[0],i + d[1]).isWalkable() && !(map.getTileByIndex(j + d[0] * 2,i + d[1] * 2).isWalkable())){
                            directionsBlocked++;
                        }
                    }
                    if(directionsBlocked == 3) deadEnds.add(new int[]{j, i});
                }
            }

        }
        return deadEnds;
    }


    public Item getRandomItem() {
        List<Item> items = List.of( new YarnBall(), new SnickersBar(), new CO2Shears(), new CO2Laser(),
                new BlindingStew(), new RustyPogoStick(),new UltimateObliterator());
        List<Integer> weights = List.of(20, 20, 17, 15, 15, 8, 5);
        Random random = new Random();

        int randomInt = random.nextInt(1, 101);
        int i = 0;
        while (randomInt > 0) {
            randomInt = randomInt - weights.get(i);
            if (randomInt <= 0) {
                break;
            }
            i++;
        }

        return items.get(i);
    }


    public boolean isInbounds(int x, int y, int mapsize) {
        return (x >= 0 && x < mapsize && y >= 0 && y < mapsize);
    }

    public int getRandomTextureInt() {
        Random random = new Random();
        return random.nextInt(0, 4);
    }
}

