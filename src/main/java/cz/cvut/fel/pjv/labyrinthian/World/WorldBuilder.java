package cz.cvut.fel.pjv.labyrinthian.World;

import cz.cvut.fel.pjv.labyrinthian.Components.Utils;
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

/**
 * Generates all game world content — the maze, boss arena, enemies, clay pots, and portals.
 * <p>
 * Map layout convention: {@code tiles[X][Y]}
 * The maze is generated using the recursive backtracker algorithm with 3-tile-wide corridors.
 * The boss arena is a circular clearing carved in the center of the map.
 */
public class WorldBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(WorldBuilder.class);

    /**
     * Generates a new square maze map with the given side length.
     * Requires mapSize ≥ 22 to fit the boss arena.
     *
     * @param mapSize side length of the map in tiles
     * @return the generated map
     * @throws IllegalArgumentException if mapSize < 22
     */
    public Map buildMap(int mapSize) {
        LOG.info("Starting map generation, size: {}x{}", mapSize, mapSize);
        if (mapSize < 22) throw new IllegalArgumentException("Map size must be at least 22");

        // Initialize all tiles as hedge
        Tile[][] tiles = new Tile[mapSize][mapSize];
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                tiles[x][y] = new Tile(TileType.HEDGE, 1);
            }
        }
        //starts by carving out boss arena
        addBossArena(tiles, mapSize);
        //builds the maze around the boss arena
        generateMaze(tiles, mapSize);

        // Ensure map border is always solid wall
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                if (x == mapSize - 1 || y == mapSize - 1 || x == 0 || y == 0) {
                    tiles[x][y] = new Tile(TileType.ARENA_WALL, 1);
                }
            }
        }

        return new Map(tiles, mapSize);
    }

    /**
     * Generates the maze using the recursive backtracker (DFS) algorithm.
     * Moves in steps of 4 tiles to create 3-tile-wide corridors.
     * selects a random direction and burrows until it finds a dead end,
     * then starts returning until it finds a tile with unexplored directions
     * Skips cells that would carve into the boss arena.
     * <p>
     * Luckily, and as a rather nice surprise to me, this version of the algorithm also naturally
     * generates "loot rooms" (3x3 dead ends that sometimes branch off the main path)
     * which are ideal for spawning claypots
     * </p>
     */
    private void generateMaze(Tile[][] tiles, int mapSize) {
        LOG.debug("Generating maze with recursive backtracker algorithm");
        //stack for FiLo approach
        Deque<int[]> stack = new ArrayDeque<>();
        //keeps track of already visited spaces so they aren't readded to queue
        boolean[][] visited = new boolean[mapSize][mapSize];
        tiles[1][1] = new Tile(TileType.PATH, getRandomTextureInt());
        visited[1][1] = true;
        stack.push(new int[]{1, 1}); // start at tile (1,1)

        while (!stack.isEmpty()) {
            //takes tile at the top of the stacks
            int[] stackTop = stack.peek();
            //takes valid "neighbors" - tiles from the 4 cardinal directions with a gap of 3 from the starting tile
            List<int[]> neighbours = getUnvisitedNeighbors(tiles, stackTop[0], stackTop[1], visited, mapSize);

            if (!neighbours.isEmpty()) {
                Random random = new Random();
                //picks a random neighbor as direction
                int[] randomNeighbour = neighbours.get(random.nextInt(neighbours.size()));
                // Carve the 3-wide corridor connecting the two cells
                List<int[]> connectingTiles = getConnectingPath(tiles, mapSize, stackTop, randomNeighbour);
                for (int[] cord : connectingTiles) {
                    if (tiles[cord[0]][cord[1]].getTile() != TileType.ARENA_WALL) {
                        tiles[cord[0]][cord[1]] = new Tile(TileType.PATH, getRandomTextureInt());
                    }
                }
                //change the neighbor to path, set visited to true and add to stack
                tiles[randomNeighbour[0]][randomNeighbour[1]] = new Tile(TileType.PATH, getRandomTextureInt());
                visited[randomNeighbour[0]][randomNeighbour[1]] = true;
                stack.push(randomNeighbour);
            } else {
                stack.pop(); // dead end - backtrack
            }
        }
    }

    /**
     * Carves a circular boss arena in the center of the map.
     * The arena interior is open PATH, surrounded by a ring of ARENA_WALL tiles.
     * An entrance corridor is opened on the left side.
     */
    private void addBossArena(Tile[][] tiles, int mapSize) {
        final int radius = 10;
        final int centerX = mapSize / 2;
        final int centerY = mapSize / 2;

        // Outer ring of arena wall
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                if (Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)) <= radius + 1) {
                    tiles[x][y] = new Tile(TileType.ARENA_WALL, 1);
                }
            }
        }
        // Inner arena floor
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

    /**
     * Spawns a list of enemies at random valid positions on the map such that they don't spawn in the boss arena
     * and that they are spaced out.
     * Enemy health scales exponentially using the  formula {@code 4 * 1.5 ^ n}, where n is the number of the current level.
     * This makes levels 1-5 relatively easy and gets exponentially harder.
     * Game starts getting borderline impossible around level 30 - this is by design.
     *
     * @param count  number of enemies to spawn
     * @param map    the game map
     * @param scale  level scaling factor for enemy health and damage
     * @return list of spawned enemies
     */
    public List<Enemy> buildEnemies(int count, Map map, double scale) {
        double healthScale = 4 * Math.pow(1.5, scale);
        LOG.info("Spawning {} enemies", count);
        List<Enemy> enemyList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int[] enemyCords = getRandomPosition(map, enemyList);
            //damage and health scale differently, damage scaling is linear
            Enemy enemy = new Enemy(enemyCords[0] * 64, enemyCords[1] * 64, 48, 48, healthScale, scale, 3, 80);
            LOG.debug("Enemy {} spawned at ({}, {})", i + 1, enemyCords[0], enemyCords[1]);
            enemyList.add(enemy);
        }
        return enemyList;
    }

    /**
     * Spawns the boss in the center of the map with stats scaled to the level.
     *
     * @param map   the game map
     * @param scale level scaling factor
     * @return the spawned boss
     */
    public Boss spawnBoss(Map map, double scale) {
        double healthScale = 4 * Math.pow(1.5, scale);
        return new Boss(map.getWidth() * 32, map.getHeight() * 32, 196, 196, 4 * healthScale, scale * 2, 2, 0);
    }

    /**
     * Randomly spawns clay pots in "loot rooms" around the map
     * Each pot contains a semi-randomly chosen items - items are weighed by rarity.
     *
     * @param count number of pots to spawn
     * @param map   the game map
     * @return list of spawned clay pots
     */
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

    /**
     * Creates an escape portal at the given pixel coordinates.
     *
     * @param cordX x position in pixels
     * @param cordY y position in pixels
     * @return the new escape portal
     */
    public EscapePortal buildPortal(double cordX, double cordY) {
        return new EscapePortal(cordX, cordY);
    }

    /**
     * Returns all tile coordinates of the connecting corridor between two maze cells.
     * Creates a 3-tile-wide path by including one tile on each side of the main axis.
     */
    private List<int[]> getConnectingPath(Tile[][] tiles, int mapSize, int[] source, int[] neighbour) {
        int stepsX = Math.abs(neighbour[0] - source[0]);
        int stepsY = Math.abs(neighbour[1] - source[1]);
        int pathStartX = Math.min(source[0], neighbour[0]);
        int pathStartY = Math.min(source[1], neighbour[1]);
        List<int[]> connectingTiles = new ArrayList<>();

        if (stepsX == 0) { // moving along Y axis — widen horizontally
            for (int i = 0; i < stepsY; i++) {
                connectingTiles.add(new int[]{pathStartX, pathStartY + i});
                if (isInbounds(pathStartX + 1, pathStartY, mapSize))
                    connectingTiles.add(new int[]{pathStartX + 1, pathStartY + i});
                if (isInbounds(pathStartX - 1, pathStartY, mapSize))
                    connectingTiles.add(new int[]{pathStartX - 1, pathStartY + i});
            }
        } else { // moving along X axis — widen vertically
            for (int i = 0; i < stepsX; i++) {
                connectingTiles.add(new int[]{pathStartX + i, pathStartY});
                if (isInbounds(pathStartX, pathStartY + 1, mapSize))
                    connectingTiles.add(new int[]{pathStartX + i, pathStartY + 1});
                if (isInbounds(pathStartX, pathStartY - 1, mapSize))
                    connectingTiles.add(new int[]{pathStartX + i, pathStartY - 1});
            }
        }
        return connectingTiles;
    }

    /**
     * Returns unvisited maze cell candidates reachable in 4 directions (gap of 4).
     * Skips any path that would cross through the boss arena.
     */
    private List<int[]> getUnvisitedNeighbors(Tile[][] tiles, int x, int y, boolean[][] visited, int mapSize) {
        List<int[]> neighboursList = new ArrayList<>();
        int[][] directions = {{-4, 0}, {4, 0}, {0, -4}, {0, 4}};
        for (int[] direction : directions) {
            int newCordX = x + direction[0];
            int newCordY = y + direction[1];
            if (isInbounds(newCordX, newCordY, mapSize)) {
                List<int[]> connectingTiles = getConnectingPath(tiles, mapSize,
                        new int[]{x, y}, new int[]{newCordX, newCordY});
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

    /**
     * Finds a random walkable position on the map not too close to other entities,
     * the map center, or the player spawn point.
     * <p>
     * Loops until a valid position is found.
     *
     * @param map      the game map
     * @param entities existing entities to keep distance from
     * @return tile coordinates as int[]{x, y}
     */
    public int[] getRandomPosition(Map map, List<? extends Entity> entities) {
        int[] positionXY = new int[2];
        Random random = new Random();
        boolean foundValid = false;

        while (!foundValid) {
            boolean isOccupied = false;
            int xPos = random.nextInt(0, map.getWidth());
            int yPos = random.nextInt(0, map.getHeight());

            //checks if too close to player spawn or inside boss arena
            if (Utils.distance(xPos * 64, yPos * 64, map.getMapSize() * 32, map.getMapSize() * 32) < 2000
                    || Utils.distance(xPos * 64, yPos * 64, 64, 64) < 500) {
                isOccupied = true;

            }
            if(!isOccupied){
                for (Entity e : entities) {
                    // Checks if too close to another entity
                    if(Utils.distance(xPos * 64, yPos * 64, e.getCordX(), e.getCordY()) < 1000){
                        isOccupied = true;
                        break;
                    }
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

    /**
     * Finds walkable tiles that are in a "loot room" shape, which doesn't have to be a traditional dead end.
     * I consider a loot room/dead end a 3x3 area of path tiles that has three of its directions blocked.
     * If the corners of the box are a hedge in the direction away from the path, it's also considered a loot room for simplicity.
     *
     * @param map the game map
     * @return list of dead-end tile coordinates
     */
    private List<int[]> findDeadEnds(Map map) {
        List<int[]> deadEnds = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int i = 0; i < map.getMapSize(); i++) {
            for (int j = 0; j < map.getMapSize(); j++) {
                //skips hedges
                if (!map.getTileByIndex(j, i).isWalkable()) continue;
                int directionsBlocked = 0;
                for (int[] d : directions) {
                    //tile in direction must be path and the one behind it hedge
                    if (map.getTileByIndex(j + d[0], i + d[1]).isWalkable()
                            && !(map.getTileByIndex(j + d[0] * 2, i + d[1] * 2).isWalkable())) {
                        directionsBlocked++;
                    }
                }
                if (directionsBlocked == 3) deadEnds.add(new int[]{j, i});
            }
        }
        return deadEnds;
    }

    /**
     * Returns a semi-randomly selected item using a weighted probability table.
     * Common items (YarnBall, SnickersBar) have higher weights;
     * the UltimateObliterator has the lowest weight.
     *
     * @return a new instance of a randomly chosen item
     */
    public Item getRandomItem() {
        List<Item> items = List.of(new YarnBall(), new SnickersBar(), new CO2Shears(),
                new CO2Laser(), new BlindingStew(), new RustyPogoStick(), new UltimateObliterator());
        List<Integer> weights = List.of(20, 20, 17, 15, 15, 8, 5);
        Random random = new Random();

        // Weighted random selection
        int randomInt = random.nextInt(1, 101);
        int i = 0;
        while (randomInt > 0) {
            randomInt = randomInt - weights.get(i);
            if (randomInt <= 0) break;
            i++;
        }
        return items.get(i);
    }

    /**
     * Checks whether the given tile index is within the map bounds.
     *
     * @param x       tile x index
     * @param y       tile y index
     * @param mapsize map side length
     * @return true if in bounds
     */
    public boolean isInbounds(int x, int y, int mapsize) {
        return (x >= 0 && x < mapsize && y >= 0 && y < mapsize);
    }

    /**
     * Returns a random texture variant index for path tiles (0–3).
     *
     * @return random integer in [0, 4)
     */
    public int getRandomTextureInt() {
        return new Random().nextInt(0, 4);
    }
}
