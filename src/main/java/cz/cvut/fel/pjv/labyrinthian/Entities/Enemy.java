package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Components.Utils;
import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Core.GameState;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * An AI-controlled enemy that guards the maze, detects the player via line-of-sight
 * and chases/attacks them using A* pathfinding.
 * <p>
 * State logic: IDLE -> CHASING -> ATTACKING -> RETURN -> IDLE
 */
public class Enemy extends Entity {

    private static final int CHASE_TIMER_FRAMES = 120;
    private static final int EXTENDED_CHASE_THRESHOLD = 15;
    private static final int MAX_CHASE_DISTANCE_TILES = 40;
    private static final double ATTACK_ENGAGE_DISTANCE = 80;
    private static final int ATTACK_COOLDOWN_MULTIPLIER = 30;
    private static final double MOVE_STEP_SIZE = 3;
    private static final int ASTAR_MAX_STEPS = 400;
    private static final int ASTAR_INITIAL_SCORE = 99999;
    private static final int TILE_CENTER_OFFSET = 32;

    protected static final Logger LOG = LoggerFactory.getLogger(Enemy.class);
    protected double baseDamage;
    protected int attackSpeed;
    protected EnemyState state = EnemyState.IDLE;
    protected double startX, startY;
    /** Frames remaining before the enemy gives up the chase after losing LoS (~2 seconds at 60fps). */
    protected int chaseTimer = CHASE_TIMER_FRAMES;
    protected int attackCooldown;
    /** Last known player position, used to keep chasing after losing line-of-sight,
     * for example if player tried to hide behind corner. */
    protected double lastKnownX = 0;
    protected double lastKnownY = 0;


    public Enemy(double cordX, double cordY, double height, double width,
                 double maxHealth, double baseDamage, int attackSpeed, double attackRange) {
        super(cordX, cordY, height, width, maxHealth, attackRange);
        this.baseDamage = baseDamage;
        this.attackSpeed = attackSpeed;
        this.startX = cordX;
        this.startY = cordY;
        this.attackCooldown = CHASE_TIMER_FRAMES;
    }

    /**
     * Called when this enemy dies - awards score and triggers upgrade dialogue -
     * player can either gain an extra heart or add (n^2)/10 damage to their weapon (n is current level nummber)
     *
     * @param gameManager the game manager
     */
    @Override
    public void onDeath(GameManager gameManager) {
        gameManager.getGamestats().addKillScore(false, false);
        gameManager.getDialogScreen().showUpgradeDialog();
        gameManager.setCurrentState(GameState.DIALOGUE);
    }


    public EnemyState getState() { return state; }

    /**
     * Executes one frame of AI logic.
     * <p>
     * State transitions:
     * <ul>
     *   <li>IDLE: detects player via LoS within {@code chaseThreshold} tiles</li>
     *   <li>CHASING: pathfinds to player; extends threshold to 15 tiles while already chasing</li>
     *   <li>ATTACKING: deals damage when in range; switches back to CHASING if out of range</li>
     *   <li>RETURN: pathfinds back to spawn point</li>
     * </ul>
     *
     * @param player          the player to target
     * @param map             the current game map
     * @param gameManager     the game manager
     * @param chaseThreshold  tile distance at which enemy starts chasing
     */
    public void takeTurn(Player player, Map map, GameManager gameManager, int chaseThreshold) {
        // Convert pixel distance to tile distance
        int distanceToPlayer = (int) ((Utils.distance(player.getCordX(), player.getCordY(), cordX, cordY)) / 64);
        int distanceToOrigin = (int) ((Utils.distance(startX, startY, cordX, cordY)) / 64);

        boolean hasLoS = hasLineOfSight(player, map);

        // Already chasing enemies have a longer detection range
        int effectiveChaseThreshold = state == EnemyState.CHASING ? EXTENDED_CHASE_THRESHOLD : chaseThreshold;

        switch (state) {
            case IDLE -> {
                attackCooldown = attackSpeed * ATTACK_COOLDOWN_MULTIPLIER;
                EnemyState prev = state;
                state = (distanceToPlayer <= effectiveChaseThreshold) && hasLoS ? EnemyState.CHASING : EnemyState.IDLE;
                if (distanceToOrigin > 1) state = EnemyState.RETURN;
                if (state != prev) LOG.debug("Enemy transitioned from IDLE to {}", state);
            }

            case CHASING -> {
                attackCooldown = attackSpeed * ATTACK_COOLDOWN_MULTIPLIER;
                // Give up if enemy wandered too far from spawn, stops player from leading the enemy around the map
                if (distanceToOrigin >= MAX_CHASE_DISTANCE_TILES) {
                    LOG.debug("Enemy exceeded max chase distance, returning to origin");
                    state = EnemyState.RETURN;
                }
                // Switch to attack when close enough
                if (Utils.distance(player.getCordX(), player.getCordY(), cordX, cordY) < ATTACK_ENGAGE_DISTANCE) {
                    LOG.debug("Enemy in attack range, switching to ATTACKING");
                    state = EnemyState.ATTACKING;
                }

                if (hasLoS) {
                    chaseTimer = CHASE_TIMER_FRAMES;
                    lastKnownX = player.getCordX();
                    lastKnownY = player.getCordY();
                    if (!moveTo(player.getCordX(), player.getCordY(), map)) {
                        LOG.debug("Enemy pathfinding failed while chasing, returning to origin");
                        state = EnemyState.RETURN;
                    }
                } else {
                    // Continue toward last known position of player until chase time expires
                    if (--chaseTimer == 0) {
                        LOG.debug("Enemy lost sight of player, chase timer expired, returning");
                        state = EnemyState.RETURN;
                    } else {
                        moveTo(lastKnownX, lastKnownY, map);
                    }
                }
            }

            case RETURN -> {
                // can't re-agro before reaching default position
                attackCooldown = attackSpeed * ATTACK_COOLDOWN_MULTIPLIER;
                if (distanceToOrigin <= 1) {
                    LOG.debug("Enemy reached origin, switching to IDLE");
                    state = EnemyState.IDLE;
                } else {
                    if (!moveTo(startX, startY, map)) {
                        LOG.debug("Enemy pathfinding failed while returning to origin");
                    }
                }
            }

            case ATTACKING -> {
                attackCooldown--;
                if (distanceToPlayer * GameManager.TILE_SIZE > attackRange || !hasLoS) {
                    state = EnemyState.CHASING;
                } else {
                    if (attackCooldown <= 0) {
                        LOG.debug("Enemy attacked player for {} damage", baseDamage);
                        player.takeDamage(baseDamage, gameManager);
                        attackCooldown = attackSpeed * ATTACK_COOLDOWN_MULTIPLIER;
                    }
                }
            }
        }
    }

    /**
     * Moves toward a target pixel position using A* pathfinding.
     * Takes a single step per frame to maintain smooth movement.
     *
     * @param dx  target x in pixels
     * @param dy  target y in pixels
     * @param map the current map
     * @return true if a valid path step was taken, false if pathfinding failed
     */
    private boolean moveTo(double dx, double dy, Map map) {
        int[] destTile = seekTarget(dx, dy, map);
        if (destTile == null) {
            LOG.debug("seekTarget returned null, target unreachable at ({}, {})", (int) (dx / GameManager.TILE_SIZE), (int) (dy / GameManager.TILE_SIZE));
            return false;
        }
        double destTileX = (double) destTile[0] * GameManager.TILE_SIZE + TILE_CENTER_OFFSET;
        double destTileY = (double) destTile[1] * GameManager.TILE_SIZE + TILE_CENTER_OFFSET;
        double dirX = destTileX - cordX;
        double dirY = destTileY - cordY;
        // Clamp step to 3px or remaining distance to prevent overshoot
        double stepX = Math.abs(dirX) < MOVE_STEP_SIZE ? dirX : Math.signum(dirX) * MOVE_STEP_SIZE;
        double stepY = Math.abs(dirY) < MOVE_STEP_SIZE ? dirY : Math.signum(dirY) * MOVE_STEP_SIZE;
        move(stepX, 0, map);
        move(0, stepY, map);
        return true;
    }

    /**
     * A* pathfinding - uses priority queue and heuristic representation of the whole map.
     * Iteratively checks all neighboring tiles,priority queue first checks tiles that are in the direction of the player,
     * once player is found, algorithm reconstructs the path and returns the first step,
     * Uses Manhattan distance as heuristic. Limited to 400 steps to prevent freezing.
     *
     * @param dx  target x in pixels
     * @param dy  target y in pixels
     * @param map the current map
     * @return tile coordinates of the next step, or null if unreachable
     */
    private int[] seekTarget(double dx, double dy, Map map) {
        /** map of tiles where each tile holds coordinates of its "parent" tile i.e. tile from which the algorithm stepped onto the current tile */
        int[][][] parent = new int[map.getWidth()][map.getHeight()][2];
        PriorityQueue<int[]> tilesToCheck = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
        int[] enemyStart = {(int) this.cordX / GameManager.TILE_SIZE, (int) this.cordY / GameManager.TILE_SIZE};
        //all tiles are given a "cost" which is set to a large number at the beginning
        int[][] gScore = new int[map.getWidth()][map.getHeight()];
        for (int[] row : gScore) Arrays.fill(row, ASTAR_INITIAL_SCORE);
        //starting position set to 0
        gScore[enemyStart[0]][enemyStart[1]] = 0;

        int[] targetPos = {(int) (dx / GameManager.TILE_SIZE), (int) (dy / GameManager.TILE_SIZE)};
        int steps = 0;
        //add starting tile to queue
        tilesToCheck.add(enemyStart);
        int[] nextStep = null;
        int[] currentStep = new int[]{targetPos[0], targetPos[1]};
        //limited amount of steps to save on computing power
        while (!tilesToCheck.isEmpty() && steps < ASTAR_MAX_STEPS) {
            steps++;
            //take tile from the top of the queue - one with the smallest sum of gscore and heuristic(distance to target)
            //this makes the algorithm more efficient by naturally preferring tiles in direction to target
            int[] actual = tilesToCheck.poll();
            if (actual[0] == targetPos[0] && actual[1] == targetPos[1]) break;
            //check all neighboring tiles
            for (int[] n : getNeighbors(actual[0], actual[1], map)) {
                // checks if cost to neighbor is one step larger if yes then updates gscore and parent cords of neighbor tile
                //if no then better path to this tile was already found -> ignores
                if (gScore[actual[0]][actual[1]] + 1 < gScore[n[0]][n[1]]) {
                    gScore[n[0]][n[1]] = gScore[actual[0]][actual[1]] + 1;
                    parent[n[0]][n[1]] = actual;
                    //adds the tile to queue
                    tilesToCheck.add(new int[]{n[0], n[1], gScore[n[0]][n[1]] + heuristic(n[0], n[1], targetPos)});
                }
            }
        }

        // Trace back from target to find the first step
        while (!(currentStep[0] == enemyStart[0] && currentStep[1] == enemyStart[1])) {
            nextStep = currentStep;
            currentStep = parent[currentStep[0]][currentStep[1]];
            // Guard against infinite loop if path trace fails
            if (currentStep[0] == 0 && currentStep[1] == 0 &&
                    !(enemyStart[0] == 0 && enemyStart[1] == 0)) {
                return null;
            }
        }
        return nextStep;
    }

    /**
     * Manhattan distance heuristic for A*.
     *
     * @param x      current tile x
     * @param y      current tile y
     * @param target target tile coordinates
     * @return estimated distance in tiles
     */
    private int heuristic(int x, int y, int[] target) {
        return Math.abs(x - target[0]) + Math.abs(y - target[1]);
    }

    /**
     * Returns all walkable neighboring tiles (8-directional).
     * For diagonal moves, both adjacent tiles must also be walkable
     * to prevent clipping through corners.
     *
     * @param x   current tile x
     * @param y   current tile y
     * @param map the current map
     * @return list of valid neighbor tile coordinates
     */
    private List<int[]> getNeighbors(int x, int y, Map map) {
        List<int[]> neighboursList = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {1, -1}, {-1, 1}, {1, 1}};
        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];
            if (map.isInboundsByIndex(newX, newY) && map.getTileByIndex(newX, newY).isWalkable()) {
                boolean isDiagonal = direction[0] != 0 && direction[1] != 0;
                if (isDiagonal) {
                    // Both adjacent tiles must be walkable to allow diagonal movement
                    if (!map.getTileByIndex(x + direction[0], y).isWalkable()) continue;
                    if (!map.getTileByIndex(x, y + direction[1]).isWalkable()) continue;
                }
                neighboursList.add(new int[]{newX, newY});
            }
        }
        return neighboursList;
    }

    /**
     * Checks whether the enemy has an unobstructed line of sight to the player.
     * Fires three Bresenham rays (center + two corners) to reduce blind spots
     * caused by diagonal wall configurations.
     *
     * @param player the player to check visibility toward
     * @param map    the current map
     * @return true if at least one ray reaches the player without hitting a wall
     */
    protected boolean hasLineOfSight(Player player, Map map) {
        return bresenham(getCenterX(), getCenterY(), player.getCenterX(), player.getCenterY(), map)
                || bresenham(cordX, cordY, player.getCordX(), player.getCordY(), map)
                || bresenham(cordX + width, cordY + height,
                             player.getCordX() + player.getWidth(),
                             player.getCordY() + player.getHeight(), map);
    }

    /**
     *
     * Draws a line of tiles by walking along one axis until it deviates from a theoretical straight line by a set "error" amount
     * and corrects by making a step on the other axis.
     * Returns false if any non-walkable tile is encountered along the line.
     * This is apparently called a Bresenham line
     *
     * @param sourceX ray origin x in pixels
     * @param sourceY ray origin y in pixels
     * @param targetX ray target x in pixels
     * @param targetY ray target y in pixels
     * @param map     the current map
     * @return true if the ray reaches the target without obstruction
     */
    protected boolean bresenham(double sourceX, double sourceY, double targetX, double targetY, Map map) {
        boolean los = true;
        double error = 0;
        int dx = (int) ((targetX / GameManager.TILE_SIZE) - (sourceX / GameManager.TILE_SIZE));
        int dy = (int) ((targetY / GameManager.TILE_SIZE) - (sourceY / GameManager.TILE_SIZE));
        int currentX = (int) (sourceX / GameManager.TILE_SIZE);
        int currentY = (int) (sourceY / GameManager.TILE_SIZE);

        if (dx == 0 && dy == 0) return true;

        while (!((currentX == (int) targetX / GameManager.TILE_SIZE) && (currentY == (int) targetY / GameManager.TILE_SIZE))) {
            //checks if current tile is a path, if not, enemy does not have a line of sight.
            if (!map.getTileByIndex(currentX, currentY).isWalkable()) {
                los = false;
                break;
            }
            //chooses a direction towards target
            if (Math.abs(dx) > Math.abs(dy)) {
                currentX += (int) Math.signum(dx);
                //with every step checks how far it  strays from the ideal line, adds this different into the error
                error += (Math.abs(dx) == 0) ? 0 : (double) Math.abs(dy) / Math.abs(dx);
                //once enough error is accumulated, corrects by walking in the other direction
                if (error >= 0.5) { currentY += (int) Math.signum(dy); error -= 1.0; }
            } else {
                currentY += (int) Math.signum(dy);
                error += (Math.abs(dy) == 0) ? 0 : (double) Math.abs(dx) / Math.abs(dy);
                if (error >= 0.5) { currentX += (int) Math.signum(dx); error -= 1.0; }
            }
        }
        return los;
    }
}
