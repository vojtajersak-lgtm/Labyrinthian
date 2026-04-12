package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.Entities.Enemy;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import cz.cvut.fel.pjv.labyrinthian.World.TileType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Renderer {


    public void render(GraphicsContext gc, Map map, Player player, List<Enemy> enemyList){
        int offsetX = player.getCordX() * 64 - 512;
        int offsetY = player.getCordY() *64 - 288;

        offsetX = Math.max(0, Math.min(offsetX, map.getWidth() * 64 - 1024));
        offsetY = Math.max(0, Math.min(offsetY, map.getHeight() * 64 - 576));


        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                if(map.getTile(j, i).isWalkable()){
                    gc.setFill(Color.YELLOW);
                }else{
                    gc.setFill(Color.GREEN);
                }
                gc.fillRect(j *64 - offsetX, i * 64 - offsetY, 64, 64);
            }

        }

        gc.setFill(Color.AQUA);
        gc.fillOval(player.getCordX() * 64 - offsetX, player.getCordY() *64 - offsetY,48, 48 );

        gc.setFill(Color.RED);
        for(Enemy e : enemyList){
            gc.fillOval(e.getCordX() * 64 - offsetX, e.getCordY() *64 - offsetY, 64, 64);
        }
    }


}
