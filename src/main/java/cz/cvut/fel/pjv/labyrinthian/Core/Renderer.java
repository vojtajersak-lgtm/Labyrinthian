package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.Entities.ClayPot;
import cz.cvut.fel.pjv.labyrinthian.Entities.Enemy;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;
import cz.cvut.fel.pjv.labyrinthian.Items.LooseItem;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import cz.cvut.fel.pjv.labyrinthian.World.TileType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Renderer {


    public void render(GraphicsContext gc, Map map, Player player, List<Enemy> enemyList, List<ClayPot> Pots, List<LooseItem> looseItems, boolean mapMode){
        double offsetX = player.getCordX() - 512;
        double offsetY = player.getCordY() - 288;

        offsetX = Math.clamp(offsetX, 0, map.getWidth() * 64 - 1024);
        offsetY = Math.clamp(offsetY, 0, map.getHeight() * 64 - 576);



        if(mapMode){
            int tileSize = Math.min(1024 / map.getWidth(), 576 / map.getHeight());
            for (int i = 0; i < map.getHeight(); i++) {
                for (int j = 0; j < map.getWidth(); j++) {
                    if(map.getTileByIndex(j, i).isWalkable()){
                        gc.setFill(Color.YELLOW);
                    }else if(map.getTileByIndex(j, i).getTile() == TileType.HEDGE){
                        gc.setFill(Color.GREEN);
                    }else{
                        gc.setFill(Color.PURPLE);
                    }
                    gc.fillRect(j *tileSize, i * tileSize, tileSize, tileSize);
                }

            }

            gc.setFill(Color.AQUA);
            gc.fillOval((player.getCordX()/64) * tileSize, (player.getCordY()/64) *tileSize,tileSize /2, tileSize /2 );

            gc.setFill(Color.RED);
            for(Enemy e : enemyList){
                gc.fillOval((e.getCordX()/64) * tileSize, (e.getCordY()/64) *tileSize,tileSize , tileSize );
            }
            gc.setFill(Color.GRAY);
            for(ClayPot p : Pots){
                gc.fillOval((p.getCordX()/64) * tileSize, (p.getCordY()/64) *tileSize,tileSize , tileSize );
            }
            if(looseItems != null){
                gc.setFill(Color.PURPLE);
                for(LooseItem i : looseItems){
                    gc.fillOval((i.getCordX()/64) * tileSize, (i.getCordY()/64) *tileSize,tileSize / 2 , tileSize / 2 );
                }
            }



        }else{
            for (int i = 0; i < map.getHeight(); i++) {
                for (int j = 0; j < map.getWidth(); j++) {
                    if(map.getTileByIndex(j, i).isWalkable()){
                        gc.setFill(Color.YELLOW);
                    }else if(map.getTileByIndex(j, i).getTile() == TileType.HEDGE){
                        gc.setFill(Color.GREEN);
                    }else {
                        gc.setFill(Color.PURPLE);
                    }
                    gc.fillRect(j *64 - offsetX, i * 64 - offsetY, 64, 64);
                }

            }

            gc.setFill(Color.AQUA);
            gc.fillOval(player.getCordX() - offsetX, player.getCordY() - offsetY,32, 32);

            gc.setFill(Color.RED);
            for(Enemy e : enemyList){
                gc.fillOval(e.getCordX() - offsetX, e.getCordY() - offsetY, 64, 64);
            }
            gc.setFill(Color.GRAY);
            for(ClayPot c : Pots){
                gc.fillOval(c.getCordX() - offsetX, c.getCordY() - offsetY, 64, 64);
            }
            if(looseItems != null){
                gc.setFill(Color.PURPLE);
                for(LooseItem l : looseItems){
                    gc.fillOval(l.getCordX() - offsetX, l.getCordY() - offsetY, 32, 32);
                }
            }
        }

    }


}
