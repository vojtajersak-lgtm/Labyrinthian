package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.Components.Utils;
import cz.cvut.fel.pjv.labyrinthian.Entities.ClayPot;
import cz.cvut.fel.pjv.labyrinthian.Entities.Enemy;
import cz.cvut.fel.pjv.labyrinthian.Entities.Entity;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.Consumable;
import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.YarnBall;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;
import cz.cvut.fel.pjv.labyrinthian.Items.LooseItem;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.Sword;
import cz.cvut.fel.pjv.labyrinthian.World.EscapePortal;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import cz.cvut.fel.pjv.labyrinthian.World.WorldBuilder;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GameManager {

    private Player mainCharacter;
    private Map map;
    private InputManager inputManager;
    private WorldBuilder worldBuilder = new WorldBuilder();
    private EscapePortal escapePortal;
    private List<Enemy> enemyList;
    private List<ClayPot> clayPots;
    private List<LooseItem> looseItemList;
    private GameState currentState;
    private boolean mapMode = false;
    private boolean yarnBallActive = false;
    private boolean blindingStewActive = false;
    private static final Logger LOG = LoggerFactory.getLogger(GameManager.class);
    private  List<double[]> yarnBallTrail;
    private double speedMultiplier = 1.0;

    public GameManager(InputManager inputManager) {
        LOG.info("GameManager initialized");
        this.mainCharacter = new Player(64, 64, 32, 32, 80);
        this.map = worldBuilder.buildMap(72);
        this.inputManager = inputManager;
        this.escapePortal = worldBuilder.buildPortal(72);
        this.enemyList = worldBuilder.buildEnemies(5,map, 1);
        this.clayPots = worldBuilder.buildClaypots(5,map);
        this.looseItemList = new ArrayList<LooseItem>();
        this.yarnBallTrail = new ArrayList<double[]>();
        this.currentState = GameState.RUNNING;
    }

    public Player getMainCharacter() {
        return mainCharacter;
    }

    public Map getMap() {
        return map;
    }

    public EscapePortal getEscapePortal() {
        return escapePortal;
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

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(double speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public void setYarnBallActive(boolean yarnBallActive) {
        this.yarnBallActive = yarnBallActive;
    }


    public void setBlindingStewActive(boolean blindingStewActive) {
        this.blindingStewActive = blindingStewActive;
    }

    public List<double[]> getYarnBallTrail() {
        return yarnBallTrail;
    }

    public void setYarnBallTrail(List<double[]> yarnBallTrail) {
        this.yarnBallTrail = yarnBallTrail;
    }

    public boolean isMapMode() {
        return mapMode;
    }

    public boolean isYarnBallActive() {
        return yarnBallActive;
    }

    public boolean isBlindingStewActive() {
        return blindingStewActive;
    }

    public void update() {
        Set keyCodeSet = inputManager.getLastCode();
        KeyCode lastPressed =inputManager.getLastPressed();

        if (keyCodeSet.contains(KeyCode.W)) mainCharacter.move(0, -7 * speedMultiplier, map);
        if (keyCodeSet.contains(KeyCode.S)) mainCharacter.move(0, 7 * speedMultiplier, map);
        if (keyCodeSet.contains(KeyCode.A)) mainCharacter.move(-7 * speedMultiplier, 0, map);
        if (keyCodeSet.contains(KeyCode.D)) mainCharacter.move(7 * speedMultiplier, 0, map);

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

                    if(Utils.distance(mainCharacter.getCordX(), mainCharacter.getCordY(), escapePortal.getCordX(), escapePortal.getCordY()) <= 64){
                        escapePortal.onInteraction(mainCharacter, this);
                    }
                }
                case F ->{
                    mainCharacter.getInventory().removeFromInventory(mainCharacter.getActiveweapon());
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
        if(yarnBallActive) {
            Item active = mainCharacter.getInventory().getActiveItem();
            if (Utils.distance(mainCharacter.getCordX(), mainCharacter.getCordY(), yarnBallTrail.getLast()[0], yarnBallTrail.getLast()[1]) >= 32) {
                yarnBallTrail.add(new double[]{mainCharacter.getCordX(), mainCharacter.getCordY()});
                ((Consumable) mainCharacter.getInventory().getActiveItem()).decreaseUses();
            } else if (active instanceof Consumable && ((Consumable) active).usedUp()
                    || !(mainCharacter.getInventory().getActiveItem() instanceof YarnBall)) {
                yarnBallActive = false;
            }
        }



        if(currentState == GameState.LEVEL_COMPLETE) nextLevel();
    }

    public void spawnItem(Item item, double cordX, double cordY) {
        LooseItem looseItem = new LooseItem(item, cordX, cordY);
        looseItemList.add(looseItem);
    }

    public void spawnEntity(Entity entity, int cordX, int cordY) {

    }
    public void nextLevel(){


        blindingStewActive = false;
        yarnBallActive = false;
        yarnBallTrail.clear();
        map = worldBuilder.buildMap(72);
        enemyList = worldBuilder.buildEnemies(5, map, 2);
        clayPots =worldBuilder.buildClaypots(5,map);
        mainCharacter.heal(mainCharacter.getMaxHealth(), this);
        mainCharacter.setCordX(64);
        mainCharacter.setCordY(64);
        mainCharacter.setMaxHealth(mainCharacter.getDeafaultValues()[0]);
        speedMultiplier = mainCharacter.getDeafaultValues()[1];

        if(mainCharacter.getActiveweapon() instanceof Sword){
            mainCharacter.getActiveweapon().setDamage(mainCharacter.getDeafaultValues()[2]);
            mainCharacter.getActiveweapon().setRange(mainCharacter.getDeafaultValues()[3]);
        }

        escapePortal = worldBuilder.buildPortal(72);
        currentState = GameState.RUNNING;


    }



}