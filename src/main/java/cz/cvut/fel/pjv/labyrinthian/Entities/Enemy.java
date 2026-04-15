package cz.cvut.fel.pjv.labyrinthian.Entities;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import cz.cvut.fel.pjv.labyrinthian.World.Tile;
import cz.cvut.fel.pjv.labyrinthian.World.TileType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Enemy extends Entity{
    private int baseDamage;
    private int attackSpeed;
    private int attackRange;
    private EnemyState state;
    private double startX, startY;       // starting position of the enemy

    public Enemy(double cordY, double cordX,double width, double height ,int maxHealth, int baseDamage, int attackSpeed, int attackRange) {
        super(cordY, cordX,width,height,maxHealth);
        this.baseDamage = baseDamage;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;
        this.state = EnemyState.IDLE;
        this.startX = cordX;
        this.startY = cordY;
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

    public int getAttackRange() {
        return attackRange;
    }

    public void attack(Entity target){}

    public void takeTurn(Player player, Map map){
        int distanceToPlayer = (int) ((Math.abs((player.getCordX()) - (this.cordX )) +
                        Math.abs((player.getCordY()) - (this.cordY ))) / 64); // apparently something called Manhattan distance
                                                                                //gives real distance with turns instead of "flight" distance
        state = distanceToPlayer > 5 ? EnemyState.IDLE : EnemyState.CHASING;

        switch (state){
            case IDLE ->{ // does nothing

            }
            case CHASING -> { // starts chasing, return if player escapes


            }
            case RETURN -> {

            }
        }


    }

    private int[] seekPlayer(Player player, Map map){
        int[][][] parent = new int[map.getWidth()][map.getHeight()][2];
        boolean[][] visited = new boolean[map.getWidth()][map.getHeight()];
        Queue<int[]> tilesToCheck = new LinkedList<>();
        int[] enemyStart = {(int) this.cordX/64,(int) this.cordY /64};
        int[] playerPos = {(int) player.getCordX() /64,(int) player.getCordY() /64};

        tilesToCheck.add(enemyStart);
        int[] nextStep = null;
        int[] currentStep = new int[]{playerPos[0],playerPos[1]};

        while(!tilesToCheck.isEmpty()){
            int[] actual = tilesToCheck.poll();
            if (actual[0] == playerPos[0] && actual[1] == playerPos[1]){
                break;
            } else{
                List<int[]> neighbors = getUnvisitedNeighbors(actual[0],actual[1],visited,map); //gets valid neighbors in all 4 directions
                for(int[] n : neighbors){
                    tilesToCheck.add(n);
                    visited[n[0]][n[1]] = true;
                    parent[n[0]][n[1]] = actual;    // parent of neighbor n is position actual
                }
            }
        }
        while(!(currentStep[0] == enemyStart[0] && currentStep[1] == enemyStart[1])){
            nextStep = currentStep;
            currentStep = parent[currentStep[0]][currentStep[1]];
        }

        return nextStep;
    }

    private List<int[]> getUnvisitedNeighbors(int x, int y, boolean[][] visited, Map map) {
        List<int[]> neighboursList = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] direction : directions) {
            int newCordX = x + direction[0];
            int newCordY = y + direction[1];

            if (map.isInboundsByIndex(newCordX, newCordY)) {

                if (!visited[newCordX][newCordY] &&
                        (map.getTileByIndex(newCordX, newCordY).isWalkable())) { // takes tile on xy coordinates and checks if walkable
                    neighboursList.add(new int[]{newCordX, newCordY});
                }
            }
        }
        return neighboursList;
    }


}
