package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.Components.Utils;
import cz.cvut.fel.pjv.labyrinthian.Entities.ClayPot;
import cz.cvut.fel.pjv.labyrinthian.Entities.Enemy;
import cz.cvut.fel.pjv.labyrinthian.Entities.Entity;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.Consumable;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;
import cz.cvut.fel.pjv.labyrinthian.Items.LooseItem;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import cz.cvut.fel.pjv.labyrinthian.World.WorldBuilder;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GameManager {

    private Player mainCharacter;
    private Map map;
    private InputManager inputManager;
    private WorldBuilder worldBuilder = new WorldBuilder();
    private List<Enemy> enemyList;
    private List<ClayPot> clayPots;
    private List<LooseItem> looseItemList;
    private GameState currentState;
    private boolean mapMode = false;
    private static final Logger LOG = LoggerFactory.getLogger(GameManager.class);

    public GameManager(InputManager inputManager) {
        LOG.info("GameManager initialized");
        this.mainCharacter = new Player(64, 64, 32, 32, 80);
        this.map = worldBuilder.buildMap(72);
        this.inputManager = inputManager;
        this.enemyList = worldBuilder.buildEnemies(5,map);
        this.clayPots = worldBuilder.buildClaypots(5,map);
        this.looseItemList = new ArrayList<LooseItem>();
        this.currentState = GameState.MAIN_MENU;
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

    public List<ClayPot> getClayPots() {
        return clayPots;
    }

    public List<LooseItem> getLooseItemList() {
        return looseItemList;
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

        if (lastPressed != null) {
            switch(lastPressed) {
                case M -> {
                    mapMode = !mapMode;
                    LOG.debug("Map mode toggled: {}", mapMode);
                }
                case SPACE -> {
                    mainCharacter.attack(enemyList, clayPots, this);
                    LOG.debug("Player attacked in direction: {}", mainCharacter.getDirection());
                }
                case Q -> {
                    Item activeItem = mainCharacter.getInventory().getActiveItem();
                    if(activeItem != null) activeItem.use(mainCharacter, this);
                }
                case I -> {
                    for(Item i : mainCharacter.getInventory().getInventorySlots()){
                        System.out.println("" + i);
                    }
                }
                case E -> {
                    LooseItem toPickUp = null;
                    for(LooseItem l : looseItemList){
                        if(Utils.distance(mainCharacter.getCordX(), mainCharacter.getCordY(), l.getCordX(), l.getCordY()) < 30){
                            toPickUp = l;
                            break;
                        }
                    }
                    if(toPickUp != null) toPickUp.onInteraction(mainCharacter, this);
                }
                case DIGIT1 -> mainCharacter.getInventory().setActiveIndex(0);
                case DIGIT2 -> mainCharacter.getInventory().setActiveIndex(1);
                case DIGIT3 -> mainCharacter.getInventory().setActiveIndex(2);
                case DIGIT4 -> mainCharacter.getInventory().setActiveIndex(3);
                case DIGIT5 -> mainCharacter.getInventory().setActiveIndex(4);


                default -> {}
            }
            inputManager.setLastPressed(null);
        }


        for(Enemy e : enemyList){
            e.takeTurn(mainCharacter, map, this);
        }




    }

    public void spawnItem(Item item, double cordX, double cordY) {
        LooseItem looseItem = new LooseItem(item, cordX, cordY);
        looseItemList.add(looseItem);
    }

    public void spawnEntity(Entity entity, int cordX, int cordY) {

    }



}