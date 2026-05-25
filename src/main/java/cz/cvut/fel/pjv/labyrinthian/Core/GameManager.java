package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.Components.GameStats;
import cz.cvut.fel.pjv.labyrinthian.Components.Utils;
import cz.cvut.fel.pjv.labyrinthian.Entities.*;
import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.BlindingStew;
import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.Consumable;
import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.SnickersBar;
import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.YarnBall;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;
import cz.cvut.fel.pjv.labyrinthian.Items.LooseItem;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.Sword;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.UltimateObliterator;
import cz.cvut.fel.pjv.labyrinthian.UI.DialogScreen;
import cz.cvut.fel.pjv.labyrinthian.World.EscapePortal;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import cz.cvut.fel.pjv.labyrinthian.World.WorldBuilder;
import javafx.scene.input.KeyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Central game manager - owns all game states updates entities,
 * input, world generation, saving and the UI dialog system.
 * <p>
 * Called once per frame by the {@code AnimationTimer} in {@code GameApplication}.
 * The current {@link GameState} determines which logic runs each frame.
 * </p>
 */
public class GameManager {

    public static final int ENEMY_COUNT = 5;
    public static final int CLAYPOT_COUNT = 5;
    public static final int WIN_LEVEL_COUNT = 5;
    public static final int PLAYER_INITIAL_HEALTH = 6;
    public static final int PLAYER_ATTACK_RANGE = 80;
    public static final int PICKUP_RANGE = 50;
    public static final int PORTAL_INTERACT_RANGE = 120;
    public static final int NPC_INTERACT_RANGE = 200;
    public static final int SNICKERS_USE_RANGE = 120;
    public static final int YARN_BALL_TRAIL_INTERVAL = 32;
    public static final double DEFAULT_SPEED_MULTIPLIER = 1.0;

    public static final int TILE_SIZE = 64;
    public static final int PLAYER_SIZE = 32;
    public static final int DEFAULT_MAP = 72;
    public static final double PIXEL_STEP = 7;



    private static final Logger LOG = LoggerFactory.getLogger(GameManager.class);
    private GameState currentState;
    private GameStats gamestats;
    private GameTimerService timerService;
    private DialogScreen dialogScreen;
    private Player mainCharacter;
    private Map map;
    private final InputManager inputManager;
    private final WorldBuilder worldBuilder = new WorldBuilder();
    private EscapePortal escapePortal;
    private List<Enemy> enemyList;
    private Boss boss;
    private final List<Projectile> projectiles;
    private List<ClayPot> clayPots;
    private final List<LooseItem> looseItemList;
    /** Item waiting for player confirmation in the UltimateObliterator dialog. */
    private LooseItem pendingPickup;
    private boolean mapMode = false;
    private boolean yarnBallActive = false;
    private boolean blindingStewActive = false;
    /** True when the UltimateObliterator is equipped — locks player health to 1. */
    private boolean hasObliterator = false;
    private final List<double[]> yarnBallTrail;
    private double speedMultiplier = 1.0;
    private final SaveManager saveManager;

    /**
     * The game starts in {@link GameState#MAIN_MENU} until the player starts a new game.
     *
     * @param inputManager the shared input manager
     */
    public GameManager(InputManager inputManager) {
        LOG.info("GameManager initialized");
        this.gamestats = new GameStats();
        this.saveManager = new SaveManager();
        this.timerService = new GameTimerService(gamestats);
        this.inputManager = inputManager;
        this.currentState = GameState.MAIN_MENU;
        this.projectiles = new ArrayList<>();
        this.looseItemList = new ArrayList<>();
        this.yarnBallTrail = new ArrayList<>();



    }

    public SaveManager getSaveManager() { return saveManager; }
    public WorldBuilder getWorldBuilder() { return worldBuilder; }
    public GameStats getGamestats() { return gamestats; }
    public DialogScreen getDialogScreen() { return dialogScreen; }
    public void setDialogScreen(DialogScreen dialogScreen) { this.dialogScreen = dialogScreen; }
    public LooseItem getPendingPickup() { return pendingPickup; }
    public void setPendingPickup(LooseItem pendingPickup) { this.pendingPickup = pendingPickup; }
    public GameTimerService getTimerService() { return timerService; }
    public Player getMainCharacter() { return mainCharacter; }
    public Boss getBoss() { return boss; }
    public void setBoss(Boss boss) { this.boss = boss; }
    public Map getMap() { return map; }
    public EscapePortal getEscapePortal() { return escapePortal; }
    public List<Enemy> getEnemyList() { return enemyList; }
    public List<ClayPot> getClayPots() { return clayPots; }
    public List<LooseItem> getLooseItemList() { return looseItemList; }
    public boolean isHasObliterator() { return hasObliterator; }
    public void setHasObliterator(boolean hasObliterator) { this.hasObliterator = hasObliterator; }
    public GameState getCurrentState() { return currentState; }
    public void setCurrentState(GameState currentState) { this.currentState = currentState; }
    public double getSpeedMultiplier() { return speedMultiplier; }
    public void setSpeedMultiplier(double speedMultiplier) { this.speedMultiplier = speedMultiplier; }
    public List<Projectile> getProjectiles() { return projectiles; }
    public List<double[]> getYarnBallTrail() { return yarnBallTrail; }
    public boolean isMapMode() { return mapMode; }
    public boolean isYarnBallActive() { return yarnBallActive; }
    public void setYarnBallActive(boolean yarnBallActive) { this.yarnBallActive = yarnBallActive; }
    public boolean isBlindingStewActive() { return blindingStewActive; }
    public void setBlindingStewActive(boolean blindingStewActive) { this.blindingStewActive = blindingStewActive; }

    /**
     * Main game update loop — processes player input, updates all entities and handles game logic.
     * Called once per frame by the AnimationTimer.
     * <p>
     * Movement uses a held-key set (WASD) for smooth diagonal movement.
     * Single-press actions (attack, item use, interact) use lastPressed/justPressed.
     * </p>
     */
    public void update() {
        Set<KeyCode> keyCodeSet = inputManager.getLastCode();
        KeyCode lastPressed = inputManager.getLastPressed();

        // Blocks movement during the "player splatted on the ground" animation after using the POGO stick
        if (mainCharacter.getRecoveryCooldown() > 0) {
            mainCharacter.tickCooldown();
        } else {
            // Smooth WASD movement - multiple keys can be held simultaneously for diagonal movement
            if (keyCodeSet.contains(KeyCode.W)) {
                mainCharacter.move(0, -PIXEL_STEP * speedMultiplier, map);
                mainCharacter.setDirection(Directions.NORTH);
            }
            if (keyCodeSet.contains(KeyCode.S)) {
                mainCharacter.move(0, PIXEL_STEP * speedMultiplier, map);
                mainCharacter.setDirection(Directions.SOUTH);
            }
            if (keyCodeSet.contains(KeyCode.A)) {
                mainCharacter.move(-PIXEL_STEP * speedMultiplier, 0, map);
                mainCharacter.setDirection(Directions.EAST);
            }
            if (keyCodeSet.contains(KeyCode.D)) {
                mainCharacter.move(PIXEL_STEP * speedMultiplier, 0, map);
                mainCharacter.setDirection(Directions.WEST);
            }
        }

        // Single-press actions - processed once per key press
        if (lastPressed != null) {
            switch (lastPressed) {
               /*  //Debug: shows a map of the whole maze, can be uncommented if lost during testing :)
                case M -> {
                    mapMode = !mapMode;
                    LOG.debug("Map mode toggled: {}", mapMode);
                }
                */

                case SPACE -> {
                    // justPressed ensures attack fires only on first press, prevents spamming by holding space
                    if (inputManager.getJustPressed().contains(KeyCode.SPACE))
                        mainCharacter.attack(enemyList, boss, clayPots, this);
                    LOG.debug("Player attacked in direction: {}", mainCharacter.getDirection());
                }
                case Q -> {
                    // Use active item; show dialog for BlindingStew to inform player of selected effect
                    Item activeItem = mainCharacter.getInventory().getActiveItem();
                    if (activeItem != null) activeItem.use(mainCharacter, this);
                    if (activeItem instanceof BlindingStew) dialogScreen.showItemDialog(activeItem);
                }
                // Debug: print inventory contents to console - disabled during gameplay
                /*case I -> {
                    for (Item i : mainCharacter.getInventory().getInventorySlots()) {
                        System.out.println("" + i);
                    }
                }*/
                case E -> {
                    // Find nearest loose item within pickup range
                    //If Snickers item is held, and pressed near the boss, transformation is attempted and item is used up
                    //Pressing near NPC shows dialogue
                    LooseItem toPickUp = null;
                    for (LooseItem l : looseItemList) {
                        if (Utils.distance(mainCharacter.getCordX(), mainCharacter.getCordY(),
                                l.cordX(), l.cordY()) < PICKUP_RANGE) {
                            toPickUp = l;
                            break;
                        }
                    }
                    if (toPickUp != null) {
                        if (toPickUp.item() instanceof UltimateObliterator) {
                            // UltimateObliterator requires confirmation dialog before equipping
                            pendingPickup = toPickUp;
                            dialogScreen.showObliteratorDialog(toPickUp.item());
                        } else {
                            toPickUp.onInteraction(mainCharacter, this);
                            dialogScreen.showItemDialog(toPickUp.item());
                        }
                    }
                    // Interact with escape portal if nearby
                    if (escapePortal != null) {
                        if (Utils.distance(mainCharacter.getCordX(), mainCharacter.getCordY(),
                                escapePortal.getCordX(), escapePortal.getCordY()) <= PORTAL_INTERACT_RANGE) {
                            escapePortal.onInteraction(mainCharacter, this);
                        }
                    }
                    if(boss != null && boss.isTransformed() && Utils.distance(mainCharacter.getCenterX(), mainCharacter.getCenterY(), boss.getCenterX(), boss.getCenterY()) <= NPC_INTERACT_RANGE){
                        boss.onInteraction(mainCharacter,this);
                    }
                    if(boss != null && Utils.distance(mainCharacter.getCenterX(), mainCharacter.getCenterY(), boss.getCenterX(), boss.getCenterY()) <= SNICKERS_USE_RANGE){
                        if(mainCharacter.getInventory().getActiveItem() instanceof SnickersBar){
                            ((SnickersBar) mainCharacter.getInventory().getActiveItem()).attempTransofrmation(this);
                        }
                    }


                }
                case F -> mainCharacter.getInventory().removeFromInventory(); // deletes active item
                // Debug: add health, can be uncoded for testing purposes
                /*case O -> {
                    mainCharacter.setMaxHealth(mainCharacter.getMaxHealth() + 10);
                    mainCharacter.heal(mainCharacter.getMaxHealth(), this);
                }*/
                case ESCAPE -> currentState = GameState.PAUSED;
                // Select active inventory slot
                case DIGIT1 -> mainCharacter.getInventory().setActiveIndex(0);
                case DIGIT2 -> mainCharacter.getInventory().setActiveIndex(1);
                case DIGIT3 -> mainCharacter.getInventory().setActiveIndex(2);
                case DIGIT4 -> mainCharacter.getInventory().setActiveIndex(3);
                case DIGIT5 -> mainCharacter.getInventory().setActiveIndex(4);
                default -> {}
            }
            inputManager.setLastPressed(null);
        }

        // UltimateObliterator passive effect — locks max health to 1 every frame
        if (hasObliterator) {
            mainCharacter.setMaxHealth(1);
            mainCharacter.setDeafaultValues(1, 0);
            mainCharacter.setCurrHealth(1);
        }

        // Update all enemies
        for (Enemy e : enemyList) {
            e.takeTurn(mainCharacter, map, this, 5);
        }

        // Update boss and projectiles
        List<Projectile> toRemove = new ArrayList<>();
        if (boss != null) {
            boss.takeTurn(mainCharacter, map, this, 5);
            for (Projectile p : projectiles) {
                p.update(this);
                if (!p.isActive()) toRemove.add(p);
            }
        }
        // Remove inactive projectiles outside the loop to avoid ConcurrentModificationException
        projectiles.removeAll(toRemove);

        // Yarn ball trail logic — add new point every 32px of movement
        if (yarnBallActive) {
            Item active = mainCharacter.getInventory().getActiveItem();
            if (Utils.distance(mainCharacter.getCordX(), mainCharacter.getCordY(),
                    yarnBallTrail.getLast()[0], yarnBallTrail.getLast()[1]) >= YARN_BALL_TRAIL_INTERVAL) {
                yarnBallTrail.add(new double[]{mainCharacter.getCordX(), mainCharacter.getCordY()});
                ((Consumable) mainCharacter.getInventory().getActiveItem()).decreaseUses();
                if (active instanceof Consumable && ((Consumable) active).usedUp()) {
                    mainCharacter.getInventory().removeFromInventory();
                }
            } else if (active instanceof Consumable && ((Consumable) active).usedUp()
                    || !(mainCharacter.getInventory().getActiveItem() instanceof YarnBall)) {
                yarnBallActive = false;
            }
        }

        // Clear single-frame input sets
        inputManager.getJustReleased().clear();
        inputManager.getJustPressed().clear();
    }

    /**
     * Spawns a loose item at the given pixel coordinates.
     * Called by clay pots on death.
     *
     * @param item  the item to spawn
     * @param cordX x coordinate in pixels
     * @param cordY y coordinate in pixels
     */
    public void spawnItem(Item item, double cordX, double cordY) {
        LooseItem looseItem = new LooseItem(item, cordX, cordY);
        looseItemList.add(looseItem);
    }

    /**
     * Spawns the escape portal at the given pixel coordinates.
     * Called by the boss on death or transformation.
     *
     * @param cordX x coordinate in pixels
     * @param cordY y coordinate in pixels
     */
    public void spawnPortal(double cordX, double cordY) {
        this.escapePortal = worldBuilder.buildPortal(cordX, cordY);
    }

    /**
     * Removes the boss from the game and clears all active projectiles.
     * Called after the boss dies from player attacks.
     */
    public void removeBoss() {
        boss = null;
        projectiles.clear();
    }

    /**
     * Advances the game to the next level.
     * Updates statistics, regenerates the map and all entities, resets player position
     * and upgradeable stats to their baseline values.
     * Transitions to {@link GameState#WON} after 5 completed levels.
     */
    public void nextLevel() {
        gamestats.completeLevelScore();
        gamestats.resetLevel();
        double scaling = gamestats.getCurrentLevel();

        // Reset active status effects
        blindingStewActive = false;
        yarnBallActive = false;
        mainCharacter.setLifeStealActive(false);

        // Regenerate world
        yarnBallTrail.clear();
        map = worldBuilder.buildMap(DEFAULT_MAP);
        enemyList = worldBuilder.buildEnemies(ENEMY_COUNT, map, scaling);
        boss = worldBuilder.spawnBoss(map, scaling);
        clayPots = worldBuilder.buildClaypots(CLAYPOT_COUNT, map);

        // Reset player position and upgradeable stats to level baseline
        mainCharacter.heal(mainCharacter.getMaxHealth(), this);
        mainCharacter.setCordX(TILE_SIZE);
        mainCharacter.setCordY(TILE_SIZE);
        mainCharacter.setMaxHealth(mainCharacter.getDeafaultValues()[0]);
        speedMultiplier = mainCharacter.getDeafaultValues()[1];
        boss.setTransformed(false);

        this.projectiles.clear();
        this.looseItemList.clear();
        this.yarnBallTrail.clear();

        // Reset weapon damage if it was upgraded with blinding stew
        if (mainCharacter.getActiveweapon() instanceof Sword) {
            mainCharacter.getActiveweapon().setDamage(mainCharacter.getDeafaultValues()[2]);
            mainCharacter.getActiveweapon().setRange(mainCharacter.getDeafaultValues()[3]);
        }
        mainCharacter.heal(0, this);

        escapePortal = null;
        currentState = GameState.RUNNING;

        // Check win condition after 5 completed levels
        if (gamestats.getLevelsCompleted() == WIN_LEVEL_COUNT) {
            currentState = GameState.WON;
            return;
        }

        LOG.info("new level started, level: {}", gamestats.getCurrentLevel());
    }

    /**
     * Initializes the game manager and generates the first level.
     * Resets all game state for a fresh playthrough.
     * Called when the player starts a new game from the main menu.
     * Clears input state, generates the world and all entities and resets all flags.
     */
    public void startNewGame() {
        LOG.info("Resetting game for a new playthrough");
        this.gamestats = new GameStats();
        this.timerService.cancel();
        this.timerService = new GameTimerService(gamestats);
        this.timerService.start();
        yarnBallTrail.clear();
        projectiles.clear();


        // Clear any held keys from the previous session
        this.inputManager.getLastCode().clear();
        this.inputManager.getJustPressed().clear();
        this.inputManager.getJustReleased().clear();

        this.mainCharacter = new Player(TILE_SIZE, TILE_SIZE, PLAYER_SIZE, PLAYER_SIZE, PLAYER_ATTACK_RANGE);
        mainCharacter.setDirection(Directions.WEST);
        this.map = worldBuilder.buildMap(DEFAULT_MAP);
        this.escapePortal = null;
        this.enemyList = worldBuilder.buildEnemies(ENEMY_COUNT, map, 1);
        this.boss = worldBuilder.spawnBoss(map, 1);
        this.clayPots = worldBuilder.buildClaypots(CLAYPOT_COUNT, map);

        this.projectiles.clear();
        this.looseItemList.clear();
        this.yarnBallTrail.clear();

        this.mapMode = false;
        this.yarnBallActive = false;
        this.blindingStewActive = false;
        this.hasObliterator = false;
        mainCharacter.setLifeStealActive(false);
        this.speedMultiplier = DEFAULT_SPEED_MULTIPLIER;


        this.currentState = GameState.RUNNING;
    }
}