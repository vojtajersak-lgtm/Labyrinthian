package cz.cvut.fel.pjv.labyrinthian.World;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WorldBuilderTest {

    /*using small maps for fast testing*/

    @Test
    void testMapHasCorrectWidth(){
        WorldBuilder worldBuilder = new WorldBuilder();
        Map map = worldBuilder.buildMap(30);
        assertEquals(30, map.getWidth());
    }
    @Test
    void testMapHasCorrectHeight(){
        WorldBuilder worldBuilder = new WorldBuilder();
        Map map = worldBuilder.buildMap(30);
        assertEquals(30, map.getHeight());
    }

    @Test
    void testSmallMapThrowsException(){
        WorldBuilder worldBuilder = new WorldBuilder();
        assertThrows(IllegalArgumentException.class, () -> worldBuilder.buildMap(10));
    }

    @Test
    void testArenaCreated(){
        WorldBuilder worldBuilder = new WorldBuilder();
        Map map = worldBuilder.buildMap(30);
        assertEquals(TileType.PATH,map.getTileByIndex(15,15).getTile());
    }

    @Test
    void testEdgesAreHedge(){
        WorldBuilder worldBuilder = new WorldBuilder();
        Map map = worldBuilder.buildMap(30);
        for (int i = 0; i < 30; i++) {
            assertEquals(TileType.HEDGE,map.getTileByIndex(0,i).getTile());
            assertEquals(TileType.HEDGE,map.getTileByIndex(29,i).getTile());
            assertEquals(TileType.HEDGE, map.getTileByIndex(i, 0).getTile());
            assertEquals(TileType.HEDGE, map.getTileByIndex(i, 29).getTile());
        }

    }

    @Test
    void testRandomItemGivesItem(){
        WorldBuilder worldBuilder = new WorldBuilder();
        assertInstanceOf(Item.class, worldBuilder.getRandomItem());
    }



}
