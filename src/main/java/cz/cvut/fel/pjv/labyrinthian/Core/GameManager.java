package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.Entities.Enemy;
import cz.cvut.fel.pjv.labyrinthian.Entities.Entity;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.Consumable;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import cz.cvut.fel.pjv.labyrinthian.World.WorldBuilder;
import javafx.scene.input.KeyCode;

import java.util.List;
import java.util.Set;

public class GameManager {

    private Player mainCharacter;
    private Map map;
    private InputManager inputManager;
    private WorldBuilder worldBuilder = new WorldBuilder();
    private List<Enemy> enemyList;
    private GameState currentState;
    private boolean mapMode = false;
    private Enemy enemy; //FOR TESTING ONLY,REMOVE!!!
    private Enemy enemy1; //FOR TESTING ONLY,REMOVE!!!

    public GameManager(InputManager inputManager) {
        this.mainCharacter = new Player(1600, 2304, 32, 32);
        this.map = worldBuilder.buildMap(72);
        this.inputManager = inputManager;
        this.enemy = new Enemy(2304, 2304, 24, 24, 6,6,6,1);
        this.enemy1 = new Enemy(2304 + 3*64, 2304, 24, 24, 6,6,6,1);
        this.enemyList = worldBuilder.buildTestingEnemies(enemy,enemy1);
        this.currentState = GameState.MAIN_MENU;
    }

    public Player getMainCharacter() {
        return mainCharacter;
    }

    public Enemy getEnemy(){
        return enemy;
    }

    public Map getMap() {
        return map;
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public boolean isMapMode() {
        return mapMode;
    }

    public void update() {
        Set keyCodeSet = inputManager.getLastCode();
        KeyCode lastPressed =inputManager.getLastPressed();

        if (keyCodeSet.contains(KeyCode.W)) mainCharacter.move(0, -7, map);
        if (keyCodeSet.contains(KeyCode.S)) mainCharacter.move(0, 7, map);
        if (keyCodeSet.contains(KeyCode.A)) mainCharacter.move(-7, 0, map);
        if (keyCodeSet.contains(KeyCode.D)) mainCharacter.move(7, 0, map);
        if(lastPressed == KeyCode.M) {
            mapMode = !mapMode;
            inputManager.setLastPressed(null);
        }

        for(Enemy e : enemyList){
            e.takeTurn(mainCharacter, map);
        }



    }

    public void spawnItem(Item item, double cordX, double cordY) {
    }

    public void spawnEntity(Entity entity, int cordX, int cordY) {

    }

    public void addToInventory(Player player, Consumable consumable) {
    }
}
