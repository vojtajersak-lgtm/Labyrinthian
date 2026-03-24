package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import javafx.scene.input.KeyCode;

public class GameManager {

    private Player mainCharacter;
    private Map map;
    private InputManager inputManager;

    public GameManager(Player mainCharacter, Map map, InputManager inputManager) {
        this.mainCharacter = mainCharacter;
        this.map = map;
        this.inputManager = inputManager;
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
