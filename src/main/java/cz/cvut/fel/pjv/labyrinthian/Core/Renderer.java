package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.Entities.Enemy;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import cz.cvut.fel.pjv.labyrinthian.World.TileType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Renderer {


    public void render(GraphicsContext gc, Map map, Player player, List<Enemy> enemyList, boolean mapMode){
        int offsetX = player.getCordX() * 64 - 512;
        int offsetY = player.getCordY() *64 - 288;

        offsetX = Math.clamp(offsetX, 0, map.getWidth() * 64 - 1024);
        offsetY = Math.clamp(offsetY, 0, map.getHeight() * 64 - 576);



        if(mapMode){
            int tileSize = Math.min(1024 / map.getWidth(), 576 / map.getHeight());
            for (int i = 0; i < map.getHeight(); i++) {
                for (int j = 0; j < map.getWidth(); j++) {
                    if(map.getTile(j, i).isWalkable()){
                        gc.setFill(Color.YELLOW);
                    }else if(map.getTile(j, i).getTile() == TileType.HEDGE){
                        gc.setFill(Color.GREEN);
                    }else{
                        gc.setFill(Color.PURPLE);
                    }
                    gc.fillRect(j *tileSize, i * tileSize, tileSize, tileSize);
                }

            }

            gc.setFill(Color.AQUA);
            gc.fillOval(player.getCordX() * tileSize, player.getCordY() *tileSize,tileSize /2, tileSize /2 );

            gc.setFill(Color.RED);
            for(Enemy e : enemyList){
                gc.fillOval(e.getCordX() * tileSize, e.getCordY() *tileSize, tileSize, tileSize);
            }

        }else{
            for (int i = 0; i < map.getHeight(); i++) {
                for (int j = 0; j < map.getWidth(); j++) {
                    if(map.getTile(j, i).isWalkable()){
                        gc.setFill(Color.YELLOW);
                    }else if(map.getTile(j, i).getTile() == TileType.HEDGE){
                        gc.setFill(Color.GREEN);
                    }else {
                        gc.setFill(Color.PURPLE);
                    }
                    gc.fillRect(j *64 - offsetX, i * 64 - offsetY, 64, 64);
                }

            }

            gc.setFill(Color.AQUA);
            gc.fillOval(player.getCordX() * 64 - offsetX, player.getCordY() *64 - offsetY,32, 32);

            gc.setFill(Color.RED);
            for(Enemy e : enemyList){
                gc.fillOval(e.getCordX() * 64 - offsetX, e.getCordY() *64 - offsetY, 64, 64);
            }
        }

    }


}
