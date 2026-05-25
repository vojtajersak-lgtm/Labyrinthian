package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.Components.Inventory;
import cz.cvut.fel.pjv.labyrinthian.Entities.*;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;
import cz.cvut.fel.pjv.labyrinthian.Items.LooseItem;
import cz.cvut.fel.pjv.labyrinthian.Items.Weapon.UltimateObliterator;
import cz.cvut.fel.pjv.labyrinthian.World.EscapePortal;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import cz.cvut.fel.pjv.labyrinthian.World.TileType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Objects;

/**
 * Handles all rendering for the game - map, entities, HUD and effects.
 * Uses JavaFX GraphicsContext to draw onto the game canvas.
 */
public class Renderer {

    private static final int HUD_FONT_SIZE = 16;
    private static final int BOSS_SPRITE_SIZE = 196;
    private static final int CLAYPOT_DRAW_WIDTH = 49;
    private static final int CLAYPOT_DRAW_HEIGHT = 51;
    private static final int HEDGE_DRAW_SIZE = 72;
    private static final int ARENA_WALL_DRAW_WIDTH = 70;
    private static final int ARENA_WALL_DRAW_HEIGHT = 72;
    private static final double HEART_SIZE = 36;
    private static final double HEART_GAP = 10;
    private static final double SLOT_SIZE = 64;
    private static final double ACTIVE_SLOT_SIZE = 72;
    private static final double SLOT_GAP = 4;
    private static final double INVENTORY_START_X = 344;
    private static final double SLOT_Y = 502;
    private static final double WEAPON_SLOT_X = 260;
    private static final double CAMERA_OFFSET_X = 512;
    private static final double CAMERA_OFFSET_Y = 288;
    private static final double BOSS_HEALTH_BAR_OFFSET_Y = 25;
    private static final double BOSS_HEALTH_BAR_HEIGHT = 10;
    private static final double ENEMY_HEALTH_BAR_OFFSET_Y = 8;
    private static final double ENEMY_HEALTH_BAR_HEIGHT = 5;
    private static final double ACTIVE_SLOT_EXPAND = 4;
    private static final double BLINDING_VISION_RADIUS = 200.0;
    private static final double SCORE_TEXT_X = 345;
    private static final double TIME_TEXT_X = 617;
    private static final double STATS_TEXT_Y_OFFSET = 80;
    private static final int PLAYER_SPRITE_DIRECTIONS = 5;
    private static final int PLAYER_SPRITE_WIDTH_0 = 40;
    private static final int PLAYER_SPRITE_HEIGHT_0 = 50;
    private static final int PLAYER_SPRITE_SIZE_1 = 50;
    private static final int NPC_SPRITE_SIZE = 50;
    private static final int YARN_TRAIL_WIDTH = 3;
    private static final int AOE_STROKE_WIDTH = 3;


    // Screen and tile constants
    private static final int SCREEN_WIDTH = 1024;
    private static final int SCREEN_HEIGHT = 576;
    private static final int TILE_SIZE = 64;
    private static final int TILE_OVERDRAW = 2; // extra pixels for hedge/arena overlap

    // Map textures
    private final Image[] pathTiles = new Image[5];
    private Image hedgeTile;
    private Image arenaTile;

    // Entity textures
    private Image[][] playerSprites; // [weaponIndex][directionIndex]
    private final Image[] enemySprites = new Image[2]; // [0=idle, 1=attacking]
    private final Image[] bossSprites = new Image[2];  // [0=idle, 1=attacking]
    private Image npc;
    private Image clayPot;
    private Image portal;
    private Image projectile;

    // Item textures [itemIndex][0=loose, 1=inv, 2=active]
    private Image[][] itemSprites;

    // HUD textures
    private Image heartFull;
    private Image heartHalf;
    private Image heartEmpty;
    private Image slotEmpty;
    private Image slotActive;

    // Yarn ball trail
    private final List<double[]> yarnBallTrail;

    // Font for HUD text
    private final Font hudFont;

    /**
     * Loads all game textures
     */
    public Renderer(List<double[]> yarnBallTrail) {
        this.yarnBallTrail = yarnBallTrail;
        this.hudFont = Font.loadFont(getClass().getResourceAsStream("/fonts/StarCrush.ttf"), HUD_FONT_SIZE);

        loadMapTextures();
        loadEntityTextures();
        loadItemTextures();
        loadHUDTextures();
    }

    // texture loading

    /** Loads all map-related textures (path tiles, hedge, arena wall). */
    private void loadMapTextures() {
        for (int i = 0; i < pathTiles.length; i++) {
            pathTiles[i] = loadImage("/textures/maze/path_" + i + ".png");
        }
        hedgeTile = loadImage("/textures/maze/hedge.png");
        arenaTile = loadImage("/textures/maze/arena_wall.png");
    }

    /** Loads all entity textures (player, enemy, boss, claypot, portal, projectile). */
    private void loadEntityTextures() {
        playerSprites = new Image[2][PLAYER_SPRITE_DIRECTIONS];
        for (int i = 0; i < PLAYER_SPRITE_DIRECTIONS; i++) {
            playerSprites[0][i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/textures/entities/player_0_" + i + ".png")), PLAYER_SPRITE_WIDTH_0, PLAYER_SPRITE_HEIGHT_0, true, true);
            playerSprites[1][i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/textures/entities/player_1_" + i + ".png")), PLAYER_SPRITE_SIZE_1, PLAYER_SPRITE_SIZE_1, true, true);
        }
        for (int i = 0; i < bossSprites.length; i++) {
            bossSprites[i] = loadImage("/textures/entities/boss" + (i + 1) + ".png");
        }
        enemySprites[0] = loadImage("/textures/entities/enemy0.png");
        enemySprites[1] = loadImage("/textures/entities/enemy1.png");
        npc        = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/textures/entities/boss3.png")), NPC_SPRITE_SIZE, NPC_SPRITE_SIZE, true, true);
        clayPot    = loadImage("/textures/entities/claypot.png");
        portal     = loadImage("/textures/entities/portal.png");
        projectile = loadImage("/textures/entities/projectile.png");
    }

    /** Loads all item sprites (loose, inventory, active variants). */
    private void loadItemTextures() {
        String[] itemNames = {"stew", "laser", "shears", "pogo", "sn1", "yarnball", "sword", "obliterator"};
        itemSprites = new Image[itemNames.length][3];
        for (int i = 0; i < itemNames.length; i++) {
            itemSprites[i][0] = loadImage("/textures/items/" + itemNames[i] + ".png");
            itemSprites[i][1] = loadImage("/textures/items/" + itemNames[i] + "_inv.png");
            itemSprites[i][2] = loadImage("/textures/items/" + itemNames[i] + "_active.png");
        }
    }

    /** Loads HUD textures (hearts, inventory slots). */
    private void loadHUDTextures() {
        heartFull  = loadImage("/textures/HUD/heart_full.png");
        heartHalf  = loadImage("/textures/HUD/heart_half.png");
        heartEmpty = loadImage("/textures/HUD/heart_empty.png");
        slotEmpty  = loadImage("/textures/HUD/slot_empty.png");
        slotActive = loadImage("/textures/HUD/slot_empty_active.png");
    }

    /**
     * Helper to load an image from the classpath.
     *
     * @param path resource path starting with /
     * @return loaded Image
     */
    private Image loadImage(String path) {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
    }


    /**
     * Main render method - clears the canvas and draws all game elements.
     * Scales the output to fill the canvas regardless of window size.
     *
     * @param gc              graphics context to draw on
     * @param currentLevelTime elapsed time on current level in seconds
     * @param totalScore      player's current total score
     * @param map             the current game map
     * @param player          the player entity
     * @param escapePortal    the escape portal (may be null)
     * @param enemyList       list of active enemies
     * @param boss            the boss entity (may be null)
     * @param projectiles     list of active projectiles
     * @param pots            list of clay pots
     * @param looseItems      list of items lying on the ground
     * @param mapMode         if true, renders the minimap overview instead
     * @param blindingStewActive if true, applies the blinding stew darkness effect
     */
    public void render(GraphicsContext gc, long currentLevelTime, int totalScore, Map map,
                       Player player, EscapePortal escapePortal, List<Enemy> enemyList,
                       Boss boss, List<Projectile> projectiles, List<ClayPot> pots,
                       List<LooseItem> looseItems, boolean mapMode, boolean blindingStewActive) {

        // Scale canvas to fill window
        double scaleX = gc.getCanvas().getWidth() / SCREEN_WIDTH;
        double scaleY = gc.getCanvas().getHeight() / SCREEN_HEIGHT;
        gc.save();
        gc.scale(scaleX, scaleY);
        gc.setImageSmoothing(false);

        // Camera offset - center on player, clamped to map bounds
        double offsetX = Math.clamp(player.getCordX() - CAMERA_OFFSET_X, 0, map.getWidth() * TILE_SIZE - SCREEN_WIDTH);
        double offsetY = Math.clamp(player.getCordY() - CAMERA_OFFSET_Y, 0, map.getHeight() * TILE_SIZE - SCREEN_HEIGHT);
        //only for debug purposes, disabled during actual gameplay
        // I wanted to delete it beforehand in, but it has proven to be too useful
        if (mapMode) {
            renderMinimap(gc, map, player, enemyList, pots, looseItems, escapePortal);
        } else {
            renderMap(gc, map, offsetX, offsetY);
            renderEntities(gc, player, enemyList, boss, pots, looseItems, projectiles, escapePortal, offsetX, offsetY);
            renderEffects(gc, player, blindingStewActive, offsetX, offsetY);
            renderHUD(gc, player);
            renderStats(gc, currentLevelTime, totalScore);
        }

        gc.restore();
    }

    // Map rendering

    /**
     * Renders the minimap overview (tile colors only, no textures).
     */
    private void renderMinimap(GraphicsContext gc, Map map, Player player,
                               List<Enemy> enemyList, List<ClayPot> pots,
                               List<LooseItem> looseItems, EscapePortal escapePortal) {
        int tileSize = Math.min(SCREEN_WIDTH / map.getWidth(), SCREEN_HEIGHT / map.getHeight());
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                if (map.getTileByIndex(j, i).isWalkable()) gc.setFill(Color.YELLOW);
                else if (map.getTileByIndex(j, i).getTile() == TileType.HEDGE) gc.setFill(Color.GREEN);
                else gc.setFill(Color.PURPLE);
                gc.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
            }
        }
        // Player dot
        gc.setFill(Color.AQUA);
        gc.fillOval((player.getCordX() / TILE_SIZE) * tileSize, (player.getCordY() / TILE_SIZE) * tileSize, tileSize / 2.0, tileSize / 2.0);

        // Enemy dots
        gc.setFill(Color.RED);
        for (Enemy e : enemyList)
            gc.fillOval((e.getCordX() / TILE_SIZE) * tileSize, (e.getCordY() / TILE_SIZE) * tileSize, tileSize, tileSize);

        // Clay pot dots
        gc.setFill(Color.GRAY);
        for (ClayPot p : pots)
            gc.fillOval((p.getCordX() / TILE_SIZE) * tileSize, (p.getCordY() / TILE_SIZE) * tileSize, tileSize, tileSize);

        // Loose item dots
        if (looseItems != null) {
            gc.setFill(Color.PURPLE);
            for (LooseItem l : looseItems)
                gc.fillOval((l.cordX() / TILE_SIZE) * tileSize, (l.cordY() / TILE_SIZE) * tileSize, tileSize / 2.0, tileSize / 2.0);
        }

        // Portal dot
        if (escapePortal != null) {
            gc.setFill(Color.LIGHTBLUE);
            gc.fillRect((escapePortal.getCordX() / TILE_SIZE) * tileSize, (escapePortal.getCordY() / TILE_SIZE) * tileSize, tileSize / 2.0, tileSize / 2.0);
        }
    }

    /**
     * Renders the game map tiles using textures.
     */
    private void renderMap(GraphicsContext gc, Map map, double offsetX, double offsetY) {
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                double x = j * TILE_SIZE - offsetX;
                double y = i * TILE_SIZE - offsetY;
                if (map.getTileByIndex(j, i).isWalkable()) {
                    gc.drawImage(pathTiles[map.getTileByIndex(j, i).getTextureIndex()], x, y, TILE_SIZE, TILE_SIZE);
                } else if (map.getTileByIndex(j, i).getTile() == TileType.HEDGE) {
                    gc.drawImage(hedgeTile, x - TILE_OVERDRAW, y - TILE_OVERDRAW, HEDGE_DRAW_SIZE, HEDGE_DRAW_SIZE);
                } else {
                    gc.drawImage(arenaTile, x - TILE_OVERDRAW, y - TILE_OVERDRAW, ARENA_WALL_DRAW_WIDTH, ARENA_WALL_DRAW_HEIGHT);
                }
            }
        }
    }

    // Entities rendering

    /**
     * Renders all entities — player, enemies, boss, clay pots, loose items, portal and projectiles.
     */
    private void renderEntities(GraphicsContext gc, Player player, List<Enemy> enemyList,
                                Boss boss, List<ClayPot> pots, List<LooseItem> looseItems,
                                List<Projectile> projectiles, EscapePortal escapePortal,
                                double offsetX, double offsetY) {
        renderPlayer(gc, player, offsetX, offsetY);
        renderEnemies(gc, enemyList, offsetX, offsetY);
        renderBoss(gc, boss, offsetX, offsetY);
        renderProjectiles(gc, projectiles, offsetX, offsetY);
        renderClayPots(gc, pots, offsetX, offsetY);
        renderLooseItems(gc, looseItems, offsetX, offsetY);
        renderPortal(gc, escapePortal, offsetX, offsetY);
        renderYarnTrail(gc, offsetX, offsetY);
    }

    /**
     * Renders the player sprite based on weapon and direction.
     * Shows a "fallen/splatted down" after POGO stick use
     */
    private void renderPlayer(GraphicsContext gc, Player player, double offsetX, double offsetY) {
        int weaponIndex = player.getActiveweapon() instanceof UltimateObliterator ? 1 : 0;
        // Index 4 is the fall sprite
        int dirIndex = player.getRecoveryCooldown() > 0 ? 4 : player.getDirection().index;
        gc.drawImage(playerSprites[weaponIndex][dirIndex], player.getCordX() - offsetX, player.getCordY() - offsetY);
    }

    /**
     * Renders all enemies with sprites and health bars.
     */
    private void renderEnemies(GraphicsContext gc, List<Enemy> enemyList, double offsetX, double offsetY) {
        for (Enemy e : enemyList) {
            int attacking = e.getState() == EnemyState.ATTACKING ? 1 : 0;
            gc.drawImage(enemySprites[attacking], e.getCordX() - offsetX, e.getCordY() - offsetY, TILE_SIZE, TILE_SIZE);
            renderHealthBar(gc, e.getCordX() - offsetX, e.getCordY() - offsetY - ENEMY_HEALTH_BAR_OFFSET_Y, TILE_SIZE, ENEMY_HEALTH_BAR_HEIGHT, e.getCurrHealth(), e.getMaxHealth());
        }
    }

    /**
     * Renders the boss - either as NPC (if transformed) or with AOE/attack visuals.
     */
    private void renderBoss(GraphicsContext gc, Boss boss, double offsetX, double offsetY) {
        if (boss == null) return;

        if (boss.isTransformed()) {
            gc.drawImage(npc, boss.getCordX() - offsetX, boss.getCordY() - offsetY);
            return;
        }

        // AOE effect
        if (boss.isAoeActive() || boss.getAoeFlashTimer() > 0) {
            double cx = boss.getCenterX() - boss.getAoeRadius() - offsetX;
            double cy = boss.getCenterY() - boss.getAoeRadius() - offsetY;
            double diameter = boss.getAoeRadius() * 2;
            if (boss.getAoeFlashTimer() > 0) {
                gc.setFill(boss.getAoeColor());
                gc.fillOval(cx, cy, diameter, diameter);
            } else {
                gc.setStroke(boss.getAoeColor());
                gc.setLineWidth(AOE_STROKE_WIDTH);
                gc.strokeOval(cx, cy, diameter, diameter);
            }
        }

        // Boss sprite
        int attackMode = boss.getSpriteChangeTimer() > 0 ? 1 : 0;
        gc.drawImage(bossSprites[attackMode], boss.getCordX() - offsetX, boss.getCordY() - offsetY, BOSS_SPRITE_SIZE, BOSS_SPRITE_SIZE);
        renderHealthBar(gc, boss.getCordX() - offsetX, boss.getCordY() - offsetY - BOSS_HEALTH_BAR_OFFSET_Y, BOSS_SPRITE_SIZE, BOSS_HEALTH_BAR_HEIGHT, boss.getCurrHealth(), boss.getMaxHealth());
    }

    /**
     * Renders a health bar at the given position.
     *
     * @param x       top-left x of the bar
     * @param y       top-left y of the bar
     * @param width   total width of the bar
     * @param height  height of the bar
     * @param current current health value
     * @param max     maximum health value
     */
    private void renderHealthBar(GraphicsContext gc, double x, double y, double width, double height, double current, double max) {
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(x, y, width, height);
        gc.setFill(Color.RED);
        gc.fillRect(x, y, width * (current / max), height);
    }

    /** Renders all active boss projectiles. */
    private void renderProjectiles(GraphicsContext gc, List<Projectile> projectiles, double offsetX, double offsetY) {
        for (Projectile p : projectiles)
            gc.drawImage(projectile, p.getCordX() - offsetX, p.getCordY() - offsetY, p.getSize(), p.getSize());
    }

    /** Renders all clay pots. */
    private void renderClayPots(GraphicsContext gc, List<ClayPot> pots, double offsetX, double offsetY) {
        for (ClayPot c : pots)
            gc.drawImage(clayPot, c.getCordX() - offsetX, c.getCordY() - offsetY, CLAYPOT_DRAW_WIDTH, CLAYPOT_DRAW_HEIGHT);
    }

    /** Renders all loose items lying on the ground. */
    private void renderLooseItems(GraphicsContext gc, List<LooseItem> looseItems, double offsetX, double offsetY) {
        if (looseItems == null) return;
        for (LooseItem l : looseItems)
            gc.drawImage(itemSprites[l.item().getSpriteIndex()][0], l.cordX() - offsetX, l.cordY() - offsetY);
    }

    /** Renders the escape portal if it exists. */
    private void renderPortal(GraphicsContext gc, EscapePortal escapePortal, double offsetX, double offsetY) {
        if (escapePortal != null)
            gc.drawImage(portal, escapePortal.getCordX() - offsetX, escapePortal.getCordY() - offsetY);
    }

    /** Renders the yarn ball trail as a series of red line segments. */
    private void renderYarnTrail(GraphicsContext gc, double offsetX, double offsetY) {

        gc.setStroke(Color.RED);
        gc.setLineWidth(YARN_TRAIL_WIDTH);
        for (int i = 0; i < yarnBallTrail.size() - 1; i++) {
            gc.strokeLine(
                    yarnBallTrail.get(i)[0] - offsetX, yarnBallTrail.get(i)[1] - offsetY,
                    yarnBallTrail.get(i + 1)[0] - offsetX, yarnBallTrail.get(i + 1)[1] - offsetY
            );
        }
    }

    // Effect rendering

    /**
     * Renders the blinding stew darkness overlay
     */
    private void renderEffects(GraphicsContext gc, Player player, boolean blindingStewActive,
                               double offsetX, double offsetY) {
        if (!blindingStewActive) return;

        double playerScreenX = player.getCordX() - offsetX;
        double playerScreenY = player.getCordY() - offsetY;

        // Radial gradient centered on player - transparent in center, black at edges
        RadialGradient gradient = new RadialGradient(
                0, 0,
                playerScreenX / SCREEN_WIDTH,
                playerScreenY / SCREEN_HEIGHT,
                BLINDING_VISION_RADIUS / Math.min(SCREEN_WIDTH, SCREEN_HEIGHT),
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(1, Color.BLACK)
        );
        gc.setFill(gradient);
        gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    // HUD rendering

    /**
     * Renders the full HUD — hearts and inventory slots.
     *
     * @param gc     graphics context
     * @param player the player whose HUD to draw
     */
    public void renderHUD(GraphicsContext gc, Player player) {
        renderHearts(gc, player);
        renderInventory(gc, player);
    }

    /**
     * Renders player health as heart icons in the top-left corner.
     * Supports full, half and empty hearts, wrapping to multiple rows if needed.
     */
    private void renderHearts(GraphicsContext gc, Player player) {
        double heartSize = HEART_SIZE;
        double gap = HEART_GAP;
        int maxPerRow = (int) ((SCREEN_WIDTH - 10) / (heartSize + gap));

        for (int i = 0; i < player.getMaxHealth() / 2; i++) {
            double heartX = 10 + (i % maxPerRow) * (heartSize + gap);
            double heartY = 10 + ((double) i / maxPerRow) * 15;
            if (player.getCurrHealth() >= (i + 1) * 2) gc.drawImage(heartFull, heartX, heartY);
            else if (player.getCurrHealth() == i * 2 + 1) gc.drawImage(heartHalf, heartX, heartY);
            else gc.drawImage(heartEmpty, heartX, heartY);
        }
    }

    /**
     * Renders the inventory bar at the bottom center of the screen.
     * Shows the weapon slot separately to the left, then 5 item slots.
     * Active slot is drawn larger (72x72 vs 64x64) with yellow border.
     */
    private void renderInventory(GraphicsContext gc, Player player) {
        final double slotSize = SLOT_SIZE, activeSize = ACTIVE_SLOT_SIZE, gap = SLOT_GAP;
        final double startX = INVENTORY_START_X, slotY = SLOT_Y, weaponX = WEAPON_SLOT_X;

        // Weapon slot
        Image weaponSlot = player.getActiveweapon() == null
                ? slotEmpty
                : itemSprites[player.getActiveweapon().getSpriteIndex()][1];
        gc.drawImage(weaponSlot, weaponX, slotY);

        // 5 inventory slots
        for (int i = 0; i < PLAYER_SPRITE_DIRECTIONS; i++) {
            double slotX = startX + i * (slotSize + gap);
            Item item = player.getInventory().getInventorySlots()[i];
            boolean isActive = (i == player.getInventory().getActiveIndex());

            if (item == null) {
                gc.drawImage(isActive ? slotActive : slotEmpty,
                        isActive ? slotX - ACTIVE_SLOT_EXPAND : slotX,
                        isActive ? slotY - ACTIVE_SLOT_EXPAND : slotY,
                        isActive ? activeSize : slotSize,
                        isActive ? activeSize : slotSize);
            } else {
                int variant = isActive ? 2 : 1;
                gc.drawImage(itemSprites[item.getSpriteIndex()][variant],
                        isActive ? slotX - ACTIVE_SLOT_EXPAND : slotX,
                        isActive ? slotY - ACTIVE_SLOT_EXPAND : slotY,
                        isActive ? activeSize : slotSize,
                        isActive ? activeSize : slotSize);
            }
        }
    }

    /** Renders time and score text in the HUD. */
    private void renderStats(GraphicsContext gc, long currentLevelTime, int totalScore) {
        gc.setFill(Color.WHITE);
        gc.setFont(hudFont);
        gc.fillText("Time: " + currentLevelTime + "s", TIME_TEXT_X, SCREEN_HEIGHT - STATS_TEXT_Y_OFFSET);
        gc.fillText("Score: " + totalScore, SCORE_TEXT_X, SCREEN_HEIGHT - STATS_TEXT_Y_OFFSET);
    }
}