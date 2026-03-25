package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.Entities.Enemy;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import cz.cvut.fel.pjv.labyrinthian.World.WorldBuilder;
import javafx.scene.input.KeyCode;

import java.util.List;

public class GameManager {

    private Player mainCharacter;
    private Map map;
    private InputManager inputManager;
    private WorldBuilder worldBuilder = new WorldBuilder();
    private List<Enemy> enemyList;

    public GameManager(InputManager inputManager) {
        this.mainCharacter = new Player(1,1);
        this.map = worldBuilder.buildMap(32);
        this.inputManager = inputManager;
        this.enemyList = worldBuilder.buildEnemies(5,map);
    }

    public Player getMainCharacter() {
        return mainCharacter;
    }

    public Map getMap() {
        return map;
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }

    public void update(){
        KeyCode key = inputManager.getLastCode();
        if (key == KeyCode.W) mainCharacter.move(0, -1, map);
        if (key == KeyCode.S) mainCharacter.move(0, 1, map);
        if (key == KeyCode.A) mainCharacter.move(-1, 0,map);
        if (key == KeyCode.D) mainCharacter.move(1, 0,map);

        inputManager.setLastCode(null);

    }
}
