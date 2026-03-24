package cz.cvut.fel.pjv.labyrinthian.Core;

import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.World.Map;
import cz.cvut.fel.pjv.labyrinthian.World.TileType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer {
    public void render(GraphicsContext gc, Map map, Player player){
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                if(map.getTile(j, i).isWalkable()){
                    gc.setFill(Color.YELLOW);
                }else{
                    gc.setFill(Color.GREEN);
                }
                gc.fillRect(j *20, i * 20, 20, 20);
            }

        }

        gc.setFill(Color.AQUA);
        gc.fillOval(player.getCordX() * 20, player.getCordY() *20,15, 15 );
    }
}
