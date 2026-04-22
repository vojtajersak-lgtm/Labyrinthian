package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.World.Map;

import java.util.*;

public class Enemy extends Entity {
    private int baseDamage;
    private int attackSpeed;
    private EnemyState state = EnemyState.IDLE;
    private double startX, startY;       // starting position of the enemy
    int chaseTimer = 120; //how long enemy chases after loosing LoS, 120 frames or ~2 seconds
    int attackCooldown;
    private double lastKnownX = 0; //target for enemy to keep chasing after loosing LoS until timer runs out
    private double lastKnownY = 0;

    public Enemy(double cordX, double cordY, double height, double width, int maxHealth, int baseDamage,int attackSpeed ,double attackRange) {
        super(cordX, cordY, height, width, maxHealth, attackRange);
        this.baseDamage = baseDamage;
        this.attackSpeed = attackSpeed;
        this.startX = cordX;
        this.startY = cordY;
        this.attackCooldown = 0;
    }

    @Override
    public void onDeath(GameManager gameManager) {

    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public double getAttackRange() {
        return attackRange;
    }

    public void attack(Entity target) {
    }

    public void takeTurn(Player player, Map map, GameManager gameManager) {
        attackCooldown--;

        int distanceToPlayer = (int) ((Math.abs((player.getCordX()) - (this.cordX)) +
                Math.abs((player.getCordY()) - (this.cordY))) / 64); // apparently something called Manhattan distance
        //gives real distance with turns instead of "flight" distance

        int distanceToOrigin = (int) ((Math.abs((startX) - (this.cordX)) +
                Math.abs((startY) - (this.cordY))) / 64);

        boolean hasLoS = hasLineOfSight(player,map);


        int chaseThreshold = state == EnemyState.CHASING ? 15 : 5; //5 tiles to start chasing, 15 when already chasing before player escapes

        switch (state) {
            case IDLE -> {
                state = (distanceToPlayer <= chaseThreshold) && hasLoS  ? EnemyState.CHASING : EnemyState.IDLE;
                if(distanceToOrigin >= 10) state = EnemyState.RETURN;
                if(distanceToOrigin > 1) state = EnemyState.RETURN;
            }

            case CHASING -> { // starts chasing, return if player escapes
                if(distanceToOrigin >= 40) state = EnemyState.RETURN;
                if(Math.abs(player.getCordX() - cordX) < (80)
                        && Math.abs(player.getCordY() - cordY) < (80)){
                    state = EnemyState.ATTACKING; //if distance is less than 32 pixels
                }

                if(hasLoS){
                    chaseTimer = 120;
                    lastKnownX = player.getCordX();
                    lastKnownY = player.getCordY();
                    if(!moveTo(player.getCordX(), player.getCordY(), map)) {
                        state = EnemyState.RETURN;
                    }

                }else{
                    if(!hasLoS && --chaseTimer == 0){
                        state = EnemyState.RETURN;
                    }else{
                        moveTo(lastKnownX,lastKnownY,map);
                    }
                }

            }
            case RETURN -> {
                if(distanceToOrigin <= 1) state = EnemyState.IDLE;
                else {
                    moveTo(startX, startY, map);
                }
            }
            case ATTACKING -> {
                if(distanceToPlayer > attackRange || !hasLoS) state = EnemyState.CHASING;
                else{
                    if(attackCooldown <= 0 && distanceToPlayer <= attackRange){
                        player.takeDamage(baseDamage,gameManager);
                        System.out.println("Attacked player!");
                        attackCooldown = attackSpeed * 60;
                    }
                }
            }
        }


    }



    private boolean moveTo(double dx, double dy, Map map){
        int[] destTile = seekTarget(dx, dy, map);
        if(destTile == null) return false;
        double destTileX = (double) destTile[0] * 64 + 32;
        double destTileY = (double) destTile[1] * 64 + 32;
        double dirX = destTileX - cordX;
        double dirY = destTileY - cordY;
        double stepX = Math.abs(dirX) < 3 ? dirX : Math.signum(dirX) * 3; //if the difference is less than three pixels, finish the walk, prevents overshoot
        double stepY = Math.abs(dirY) < 3 ? dirY : Math.signum(dirY) * 3;
        move(stepX, 0, map);
        move(0, stepY, map);
        return true;
    }


    private int[] seekTarget(double dx, double dy, Map map) { //helper method that returns the first step towards a target, used to find path to player or a path back to starting pos
        int[][][] parent = new int[map.getWidth()][map.getHeight()][2];
        PriorityQueue<int[]> tilesToCheck = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
        int[] enemyStart = {(int) this.cordX / 64, (int) this.cordY / 64};

        int[][] gScore = new int[map.getWidth()][map.getHeight()];
        for(int[] row : gScore) Arrays.fill(row, 99999);
        gScore[enemyStart[0]][enemyStart[1]] = 0;

        int[] targetPos = {(int) (dx / 64), (int) (dy / 64)};
        int steps = 0;

        tilesToCheck.add(enemyStart);
        int[] nextStep = null;
        int[] currentStep = new int[]{targetPos[0], targetPos[1]};

        while (!tilesToCheck.isEmpty() && steps < 400) {
            steps++;
            int[] actual = tilesToCheck.poll();
            if (actual[0] == targetPos[0] && actual[1] == targetPos[1]) {
                break;
            } else {
                List<int[]> neighbors = getUnvisitedNeighbors(actual[0], actual[1], map); //gets valid neighbors in all 4 directions
                for (int[] n : neighbors) {
                    if(gScore[actual[0]][actual[1]] + 1 < gScore[n[0]][n[1]]) {
                        gScore[n[0]][n[1]] = gScore[actual[0]][actual[1]] + 1;
                        parent[n[0]][n[1]] = actual; // parent of neighbor n is position actual
                        tilesToCheck.add(new int[]{n[0], n[1], gScore[n[0]][n[1]] + heuristic(n[0], n[1], targetPos)});
                    };
                }
            }
        }
        while (!(currentStep[0] == enemyStart[0] && currentStep[1] == enemyStart[1])) {
            nextStep = currentStep;
            currentStep = parent[currentStep[0]][currentStep[1]];
            if(currentStep[0] == 0 && currentStep[1] == 0 &&  //prevents infinite loop if enemy not found which caused game to freeze
                    !(enemyStart[0] == 0 && enemyStart[1] == 0)) {
                return null;
            }
        }
        return nextStep;
    }

    private int heuristic(int x, int y, int[] target) { //helper method that returns the distance from tile to target
        return Math.abs(x - target[0]) + Math.abs(y - target[1]);
    }

    private List<int[]> getUnvisitedNeighbors(int x, int y, Map map) { //helper method, returns list valid neigbors in all four directions
        List<int[]> neighboursList = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] direction : directions) {
            int newCordX = x + direction[0];
            int newCordY = y + direction[1];

            if (map.isInboundsByIndex(newCordX, newCordY)) {

                if (map.getTileByIndex(newCordX, newCordY).isWalkable()) { // takes tile on xy coordinates and checks if walkable
                    neighboursList.add(new int[]{newCordX, newCordY});
                }
            }
        }
        return neighboursList;
    }

    private boolean hasLineOfSight(Player player, Map map) { //uses something called Bresenham line algorithm to figure if player is standing behind a wall
        boolean LoS = true;
        double error = 0;
        int dx = (int) ((player.getCordX() / 64) - (cordX / 64));
        int dy = (int) ((player.getCordY() / 64) - (cordY / 64));
        int currentX = (int)(cordX / 64);
        int currentY = (int)(cordY / 64);

        if (dx == 0 && dy == 0) return true;

        while (!((currentX == (int) player.getCordX() / 64) && (currentY == (int) player.getCordY() / 64))) {
            if (!map.getTileByIndex(currentX, currentY).isWalkable()) {
                LoS = false;
                break;
            }

            if (Math.abs(dx) > Math.abs(dy)) {
                currentX += (int) Math.signum(dx);
                error += (Math.abs(dx) == 0) ? 0 : (double) Math.abs(dy) / Math.abs(dx);

                if (error >= 0.5) {
                    currentY += (int) Math.signum(dy);
                    error -= 1.0;
                }
            } else {
                currentY += (int) Math.signum(dy);;
                error += (Math.abs(dy) == 0) ? 0 : (double) Math.abs(dx) / Math.abs(dy);

                if (error >= 0.5) {
                    currentX += (int) Math.signum(dx);
                    error -= 1.0;
                }
            }

        }
        return LoS;
    }
}

