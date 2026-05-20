package cz.cvut.fel.pjv.labyrinthian.Core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.cvut.fel.pjv.labyrinthian.Components.Inventory;
import cz.cvut.fel.pjv.labyrinthian.Entities.ClayPot;
import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.*;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.Sword;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.UltimateObliterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading the game state to/from a JSON file using Gson.
 * The save file is stored in the project root directory as {@code savegame.json}.
 */
public class SaveManager {
    private static final Logger LOG = LoggerFactory.getLogger(SaveManager.class);
    private static final String SAVE_FILE_PATH = "LabyrinthianSave.json";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Saves the current game state to {@code savegame.json}.
     * Captures player stats, inventory, enemy list, boss, clay pots,
     * portal position and game statistics.
     *
     * @param gm the game manager containing all game state
     */
    public void saveGame(GameManager gm) {
        LOG.info("Saving game into {}", SAVE_FILE_PATH);
        SaveData data = new SaveData();

        // Player state
        data.playerX = gm.getMainCharacter().getCordX();
        data.playerY = gm.getMainCharacter().getCordY();
        data.playerCurrentHp = gm.getMainCharacter().getCurrHealth();
        data.playerMaxHp = gm.getMainCharacter().getMaxHealth();
        data.lifeStealActive = gm.getMainCharacter().isLifeStealActive();
        data.playerBaseDamage = gm.getMainCharacter().getDeafaultValues()[2];
        data.playerAttackRange = gm.getMainCharacter().getDeafaultValues()[3];

        // Save inventory as item name strings
        List<String> inventoryNames = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (gm.getMainCharacter().getInventory().getInventorySlots()[i] != null) {
                inventoryNames.add(gm.getMainCharacter().getInventory().getInventorySlots()[i].toString());
            } else {
                inventoryNames.add("EMPTY");
            }
        }
        data.inventoryItemNames = inventoryNames;
        data.playerActiveWeapon = gm.getMainCharacter().getActiveweapon().toString();
        data.speedMultiplier = gm.getSpeedMultiplier();

        // Game statistics
        data.currentLevel = gm.getGamestats().getCurrentLevel();
        data.levelsCompleted = gm.getGamestats().getLevelsCompleted();
        data.totalScore = gm.getGamestats().getTotalScore();
        data.averageLevelTime = gm.getGamestats().getAverageLevelTime();

        // Save elapsed time relative to level start so timer continues correctly after load
        long currentTime = System.currentTimeMillis();
        long startTime = gm.getGamestats().getLevelStartTime();
        data.elapsedLevelTimeMillis = currentTime - startTime;

        // Active flags
        data.yarnBallActive = gm.isYarnBallActive();
        data.blindingStewActive = gm.isBlindingStewActive();
        data.hasObliterator = gm.isHasObliterator();

        // Entities
        data.enemyList = gm.getEnemyList();
        data.boss = gm.getBoss();

        // Clay pots — only positions saved; items are re-randomized on load
        data.savedClayPots = new ArrayList<>();
        for (ClayPot cp : gm.getClayPots()) {
            SaveData.ClayPotData potData = new SaveData.ClayPotData();
            potData.x = cp.getCordX();
            potData.y = cp.getCordY();
            data.savedClayPots.add(potData);
        }

        // Portal
        if (gm.getEscapePortal() != null) {
            data.portalExists = true;
            data.portalX = gm.getEscapePortal().getCordX();
            data.portalY = gm.getEscapePortal().getCordY();
        }

        // Map — exported as integer matrix
        data.mapMatrix = gm.getMap().exportMap();

        try (FileWriter writer = new FileWriter(SAVE_FILE_PATH)) {
            gson.toJson(data, writer);
            LOG.info("Game was saved.");
        } catch (IOException e) {
            LOG.error("Error during game save process: {}", e.getMessage());
        }
    }

    /**
     * Loads a saved game from {@code savegame.json} and restores the full game state.
     * Starts a fresh game first, then overwrites all values from the save file.
     *
     * @param gm the game manager to restore state into
     * @return true if loading was successful, false if the file was missing or invalid
     */
    public boolean loadGame(GameManager gm) {
        LOG.info("Loading game from {}", SAVE_FILE_PATH);

        try (FileReader reader = new FileReader(SAVE_FILE_PATH)) {
            SaveData data = gson.fromJson(reader, SaveData.class);

            if (data == null) {
                LOG.error("SaveData object after loading is null!");
                return false;
            }

            // Reset to clean state before applying saved data
            gm.startNewGame();
            gm.getMap().loadMap(data.mapMatrix);

            // Restore player stats
            gm.getMainCharacter().setCordX(data.playerX);
            gm.getMainCharacter().setCordY(data.playerY);
            gm.getMainCharacter().setMaxHealth(data.playerMaxHp);
            gm.getMainCharacter().setCurrHealth(data.playerCurrentHp);
            gm.getMainCharacter().setLifeStealActive(data.lifeStealActive);
            gm.getMainCharacter().setDeafaultValues(data.playerBaseDamage, 2);
            gm.getMainCharacter().setDeafaultValues(data.playerAttackRange, 3);

            // Restore active weapon
            switch (data.playerActiveWeapon) {
                case "Ultimate Obliterator" -> {
                    gm.getMainCharacter().setActiveweapon(new UltimateObliterator());
                    gm.setHasObliterator(true);
                }
                case "Sword" -> {
                    gm.getMainCharacter().setActiveweapon(new Sword());
                    gm.setHasObliterator(false);
                }
                default -> throw new IllegalStateException("Unexpected weapon: " + data.playerActiveWeapon);
            }

            // Restore inventory by item name
            if (data.inventoryItemNames != null) {
                Inventory inv = gm.getMainCharacter().getInventory();
                for (int i = 0; i < 5; i++) {
                    switch (data.inventoryItemNames.get(i)) {
                        case "EMPTY" -> {}
                        case "Blinding Stew"   -> inv.getInventorySlots()[i] = new BlindingStew();
                        case "CO2 Laser"       -> inv.getInventorySlots()[i] = new CO2Laser();
                        case "CO2 Shears"      -> inv.getInventorySlots()[i] = new CO2Shears();
                        case "Rusty POGO stick"-> inv.getInventorySlots()[i] = new RustyPogoStick();
                        case "SN-1C.K.E.R.S"  -> inv.getInventorySlots()[i] = new SnickersBar();
                        case "Yarn Ball"       -> inv.getInventorySlots()[i] = new YarnBall();
                    }
                }
            }

            // Restore game statistics
            gm.getGamestats().setCurrentLevel(data.currentLevel);
            gm.getGamestats().setLevelsCompleted(data.levelsCompleted);
            gm.getGamestats().setTotalScore(data.totalScore);
            gm.getGamestats().setAverageLevelTime(data.averageLevelTime);

            // Reconstruct level start time so the timer continues from where it left off
            long fakeStartTime = System.currentTimeMillis() - data.elapsedLevelTimeMillis;
            gm.getGamestats().setLevelStartTime(fakeStartTime);

            // Restore active flags
            gm.setYarnBallActive(data.yarnBallActive);
            gm.setBlindingStewActive(data.blindingStewActive);
            gm.setSpeedMultiplier(data.speedMultiplier);

            // Restore entities
            gm.getEnemyList().clear();
            if (data.enemyList != null) gm.getEnemyList().addAll(data.enemyList);
            gm.setBoss(data.boss);

            // Restore clay pots with randomized items
            gm.getClayPots().clear();
            if (data.savedClayPots != null) {
                for (SaveData.ClayPotData potData : data.savedClayPots) {
                    Item randomItem = gm.getWorldBuilder().getRandomItem();
                    gm.getClayPots().add(new ClayPot(potData.x, potData.y, 64, 64, randomItem));
                }
            }

            // Restore portal if it existed
            if (data.portalExists) {
                gm.spawnPortal(data.portalX, data.portalY);
            }

            LOG.info("Game successfully loaded");

        } catch (IOException e) {
            LOG.error("Error in game load process, save file may not exist. {}", e.getMessage());
            return false;
        }
        return true;
    }
}
