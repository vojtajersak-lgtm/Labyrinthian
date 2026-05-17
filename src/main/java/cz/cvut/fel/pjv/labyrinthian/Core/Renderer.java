package cz.cvut.fel.pjv.labyrinthian.Core;

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

import java.util.List;

public class Renderer {
    //TODO: cleanup image loading
    private final Image[] pathTiles = new Image[5];
    private final Image[][] itemSprites;
    private final Image[] bossSprites = new Image[2];
    private final Image[][] playerSprites;
    private final Image[] enemySprites = new Image[2];
    private final Image npc;
    private final Image heartFull;
    private final Image heartHalf;
    private final Image heartEmpty;
    private final Image slotEmpty;
    private final Image slotActive;
    private final Image hedgeTile;
    private final Image arenaTile;
    private final Image clayPot;
    private final Image portal;
    private final Image projectile;
    private List<double[]> yarnBallTrail;

    public Renderer(List<double[]> yarnBallTrail) {
        for (int i = 0; i < pathTiles.length; i++) {
            this.pathTiles[i] = new Image(getClass().getResourceAsStream("/textures/maze/path_" + i + ".png"));
        }
        this.hedgeTile = new Image(getClass().getResourceAsStream("/textures/maze/hedge.png"));
        this.arenaTile = new Image(getClass().getResourceAsStream("/textures/maze/arena_wall.png"));

        playerSprites = new Image[2][5];
        for (int i = 0; i < 5; i++) {
            playerSprites[0][i] = new Image(getClass().getResourceAsStream("/textures/entities/player_0_" + i + ".png"), 40, 50, true, true);
            playerSprites[1][i] = new Image(getClass().getResourceAsStream("/textures/entities/player_1_" + i + ".png"), 50, 50, true, true);

        }


        String[] itemNames = {"stew", "laser", "shears", "pogo", "sn1", "yarnball", "sword", "obliterator"};
        itemSprites = new Image[itemNames.length][3];
        for (int i = 0; i < itemNames.length; i++) {
            itemSprites[i][0] = new Image(getClass().getResourceAsStream("/textures/items/" + itemNames[i] + ".png"));
            itemSprites[i][1] = new Image(getClass().getResourceAsStream("/textures/items/" + itemNames[i] + "_inv.png"));
            itemSprites[i][2] = new Image(getClass().getResourceAsStream("/textures/items/" + itemNames[i] + "_active.png"));
        }
        for (int i = 0; i < bossSprites.length; i++) {
            bossSprites[i] = new Image(getClass().getResourceAsStream("/textures/entities/boss" + (i + 1) + ".png"));
        }

        this.enemySprites[0] = new Image(getClass().getResourceAsStream("/textures/entities/enemy0.png"));
        this.enemySprites[1] = new Image(getClass().getResourceAsStream("/textures/entities/enemy1.png"));




        this.npc = new Image(getClass().getResourceAsStream("/textures/entities/boss3.png"), 50, 50, true, true);
        this.heartFull = new Image(getClass().getResourceAsStream("/textures/HUD/heart_full.png"));
        this.heartHalf = new Image(getClass().getResourceAsStream("/textures/HUD/heart_half.png"));
        this.heartEmpty = new Image(getClass().getResourceAsStream("/textures/HUD/heart_empty.png"));
        this.slotEmpty = new Image(getClass().getResourceAsStream("/textures/HUD/slot_empty.png"));
        this.slotActive = new Image(getClass().getResourceAsStream("/textures/HUD/slot_empty_active.png"));
        this.clayPot = new Image(getClass().getResourceAsStream("/textures/entities/claypot.png"));
        this.portal = new Image(getClass().getResourceAsStream("/textures/entities/portal.png"));
        this.projectile = new Image(getClass().getResourceAsStream("/textures/entities/projectile.png"));
        this.yarnBallTrail = yarnBallTrail;


    }

    public void render(GraphicsContext gc, long currentLevelTime, int totalScore, Map map, Player player, EscapePortal escapePortal, List<Enemy> enemyList, Boss boss, List<Projectile> projectiles, List<ClayPot> Pots, List<LooseItem> looseItems, boolean mapMode, boolean blindingStewActive) {
        double offsetX = player.getCordX() - 512;
        double offsetY = player.getCordY() - 288;


        offsetX = Math.clamp(offsetX, 0, map.getWidth() * 64 - 1024);
        offsetY = Math.clamp(offsetY, 0, map.getHeight() * 64 - 576);


        if (mapMode) {
            int tileSize = Math.min(1024 / map.getWidth(), 576 / map.getHeight());
            for (int i = 0; i < map.getHeight(); i++) {
                for (int j = 0; j < map.getWidth(); j++) {
                    if (map.getTileByIndex(j, i).isWalkable()) {
                        gc.setFill(Color.YELLOW);
                    } else if (map.getTileByIndex(j, i).getTile() == TileType.HEDGE) {
                        gc.setFill(Color.GREEN);
                    } else {
                        gc.setFill(Color.PURPLE);
                    }
                    gc.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                }

            }

            gc.setFill(Color.AQUA);
            gc.fillOval((player.getCordX() / 64) * tileSize, (player.getCordY() / 64) * tileSize, tileSize / 2, tileSize / 2);

            gc.setFill(Color.RED);
            for (Enemy e : enemyList) {
                gc.fillOval((e.getCordX() / 64) * tileSize, (e.getCordY() / 64) * tileSize, tileSize, tileSize);
            }
            gc.setFill(Color.GRAY);
            for (ClayPot p : Pots) {
                gc.fillOval((p.getCordX() / 64) * tileSize, (p.getCordY() / 64) * tileSize, tileSize, tileSize);
            }
            if (looseItems != null) {
                gc.setFill(Color.PURPLE);
                for (LooseItem i : looseItems) {
                    gc.fillOval((i.getCordX() / 64) * tileSize, (i.getCordY() / 64) * tileSize, tileSize / 2, tileSize / 2);
                }
            }
            if (escapePortal != null) {
                gc.setFill(Color.LIGHTBLUE);
                gc.fillRect((escapePortal.getCordX() / 64) * tileSize, (escapePortal.getCordY() / 64) * tileSize, tileSize / 2, tileSize / 2);
            }


        } else {
            //MAP RENDERING
            for (int i = 0; i < map.getHeight(); i++) {
                for (int j = 0; j < map.getWidth(); j++) {
                    if (map.getTileByIndex(j, i).isWalkable()) {
                        gc.drawImage(pathTiles[map.getTileByIndex(j, i).getTextureIndex()], j * 64 - offsetX, i * 64 - offsetY, 64, 64);
                        continue;
                    } else if (map.getTileByIndex(j, i).getTile() == TileType.HEDGE) {
                        gc.drawImage(hedgeTile, j * 64 - offsetX - 2, i * 64 - offsetY - 2, 72, 72);
                        continue;
                    } else {
                        gc.drawImage(arenaTile, j * 64 - offsetX - 2, i * 64 - offsetY - 2, 70, 72);
                        continue;
                    }
                }

            }
            //PLAYER RENDERING
            //TODO: add player texture and direction logic
            int weaponIndex = player.getActiveweapon() instanceof UltimateObliterator ? 1 : 0;
            if(player.getRecoveryCooldown() > 0){
                gc.drawImage(playerSprites[weaponIndex][4],player.getCordX() - offsetX, player.getCordY() - offsetY);

            }else gc.drawImage(playerSprites[weaponIndex][player.getDirection().index],player.getCordX() - offsetX, player.getCordY() - offsetY);




            //ENEMY RENDERING
            gc.setFill(Color.RED);
            for (Enemy e : enemyList) {
                int attacking = e.getState() == EnemyState.ATTACKING ? 1 : 0;
                // sprite
                gc.drawImage(enemySprites[attacking], e.getCordX() - offsetX, e.getCordY() - offsetY, 64, 64);

                // healthbar background (grey)
                gc.setFill(Color.DARKGRAY);
                gc.fillRect(e.getCordX() - offsetX, e.getCordY() - offsetY - 8, 64, 5);

                // healthbar fill (red)
                double healthPct = (double) e.getCurrHealth() / e.getMaxHealth();
                gc.setFill(Color.RED);
                gc.fillRect(e.getCordX() - offsetX, e.getCordY() - offsetY - 8, 64 * healthPct, 5);
            }
            //BOSS RENDERING

            if (boss != null) {
                if (boss.isTransformed()) {

                    gc.drawImage(npc,boss.getCordX() - offsetX, boss.getCordY() - offsetY);
                } else {
                    if (boss.isAoeActive() || boss.getAoeFlashTimer() > 0) {
                        gc.setStroke(boss.getAoeColor());
                        gc.setLineWidth(3);
                        if (boss.getAoeFlashTimer() > 0) {
                            gc.setFill(boss.getAoeColor());
                            gc.fillOval(boss.getCenterX() - boss.getAoeRadius() - offsetX,
                                    boss.getCenterY() - boss.getAoeRadius() - offsetY,
                                    boss.getAoeRadius() * 2,
                                    boss.getAoeRadius() * 2);
                        } else if (boss.isAoeActive()) {
                            gc.setStroke(boss.getAoeColor());
                            gc.setLineWidth(3);
                            gc.strokeOval(boss.getCenterX() - boss.getAoeRadius() - offsetX,
                                    boss.getCenterY() - boss.getAoeRadius() - offsetY,
                                    boss.getAoeRadius() * 2,
                                    boss.getAoeRadius() * 2);
                        }
                    }
                    int attackMode = boss.getSpriteChangeTimer() > 0 ? 1 : 0;
                    gc.drawImage(bossSprites[attackMode], boss.getCordX() - offsetX, boss.getCordY() - offsetY, 196, 196);
                    gc.setFill(Color.DARKGRAY);
                    gc.fillRect(boss.getCordX() - offsetX, boss.getCordY() - offsetY - 25, 196, 10);

                    // healthbar fill (red)
                    double healthPct = (double) boss.getCurrHealth() / boss.getMaxHealth();
                    gc.setFill(Color.RED);
                    gc.fillRect(boss.getCordX() - offsetX, boss.getCordY() - offsetY - 25, 196 * healthPct, 10);
                }

            }
            //BOSS PROJECTIL ERENDERING
            for (Projectile p : projectiles) {
                gc.drawImage(projectile, p.getCordX() - offsetX, p.getCordY() - offsetY, p.getSize(), p.getSize());
            }


            //CLAYPOT RENDERING
            for (ClayPot c : Pots) {
                gc.drawImage(clayPot, c.getCordX() - offsetX, c.getCordY() - offsetY, 49, 51);
            }
            //ITEMS RENDERING
            if (looseItems != null) {
                for (LooseItem l : looseItems) {
                    gc.drawImage(itemSprites[l.getItem().getSpriteIndex()][0], l.getCordX() - offsetX, l.getCordY() - offsetY);
                }
            }
            //PORTAL RENDERING
            if (escapePortal != null) {
                gc.drawImage(portal, escapePortal.getCordX() - offsetX, escapePortal.getCordY() - offsetY);
            }

            gc.setStroke(Color.RED);
            gc.setLineWidth(3);
            for (int i = 0; i < yarnBallTrail.size() - 1; i++) {
                gc.strokeLine(yarnBallTrail.get(i)[0] - offsetX, yarnBallTrail.get(i)[1] - offsetY,
                        yarnBallTrail.get(i + 1)[0] - offsetX, yarnBallTrail.get(i + 1)[1] - offsetY);
            }
            //BLINDNESSEFFECT
            if (blindingStewActive) {
                double playerScreenX = player.getCordX() - offsetX;
                double playerScreenY = player.getCordY() - offsetY;

                RadialGradient gradient = new RadialGradient(
                        0, 0,
                        playerScreenX / 1024,
                        playerScreenY / 576,
                        200.0 / Math.min(1024, 576),
                        true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, Color.TRANSPARENT),
                        new Stop(1, Color.BLACK)
                );
                gc.setFill(gradient);
                gc.fillRect(0, 0, 1024, 576);
            }

            renderHUD(gc, player);


            gc.setFill(Color.WHITE);
            gc.fillText("Time: " + currentLevelTime + "s", 1024 - 50, 576 - 40);
            gc.fillText("Score: " + totalScore, 1024 - 75, 576 - 20);
        }

    }

    public void renderHearts(GraphicsContext gc, Player player) {
        double heartSize = 36;
        double gap = 10;
        int maxPerRow = (int) ((1024 - 10) / (heartSize + gap));

        for (int i = 0; i < player.getMaxHealth() / 2; i++) {
            double heartX = 10 + (i % maxPerRow) * (heartSize + gap);
            double heartY = 10 + (i / maxPerRow) * (15);
            if (player.getCurrHealth() >= (i + 1) * 2) {
                gc.drawImage(heartFull, heartX, heartY);
            } else if (player.getCurrHealth() == i * 2 + 1) {
                gc.drawImage(heartHalf, heartX, heartY);
            } else if (player.getCurrHealth() <= i * 2) {
                gc.drawImage(heartEmpty, heartX, heartY);
            }

        }
    }

    public void renderInventory(GraphicsContext gc, Player player) {
        double slotSize = 64, activeSize = 72, gap = 4,
                startX = 344, slotY = 502, weaponX = 260;

        Image weaponSlot = player.getActiveweapon() == null ? slotEmpty : itemSprites[player.getActiveweapon().getSpriteIndex()][1];
        gc.drawImage(weaponSlot, weaponX, slotY);


        for (int i = 0; i < 5; i++) {
            double slotX = startX + i * (slotSize + gap);
            Item item = player.getInventory().getInventorySlots()[i];
            boolean isActive = (i == player.getInventory().getActiveIndex());

            if (item == null) {
                if (isActive) {
                    gc.drawImage(slotActive, slotX - 4, slotY - 4, 72, 72);
                } else {
                    gc.drawImage(slotEmpty, slotX, slotY, 64, 64);
                }
            } else {
                if (isActive) gc.drawImage(itemSprites[item.getSpriteIndex()][2], slotX - 4, slotY - 4, 72, 72);
                else gc.drawImage(itemSprites[item.getSpriteIndex()][1], slotX, slotY, 64, 64);

            }

        }

    }

    public void renderHUD(GraphicsContext gc, Player player) {
        renderHearts(gc, player);
        renderInventory(gc, player);
    }


}
