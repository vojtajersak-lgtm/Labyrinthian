package cz.cvut.fel.pjv.labyrinthian.Components;
import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.BlindingStew;
import cz.cvut.fel.pjv.labyrinthian.Items.Consumables.YarnBall;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    @Test
    void testAddItem() {
        Inventory inventory = new Inventory();
        inventory.addItem(new BlindingStew());
        assertInstanceOf(BlindingStew.class,inventory.getInventorySlots()[0]);
    }


    @Test
    void testInventoryFull() {
        Inventory inventory = new Inventory();
        for (int i = 0; i < 5; i++) {
            inventory.addItem(new BlindingStew());
        }
        assertTrue(inventory.inventoryFull());
    }

    @Test
    void testRemoveFromInventory() {
        Inventory inventory = new Inventory();
        BlindingStew stew = new BlindingStew();
        inventory.addItem(stew);
        inventory.removeFromInventory(stew);
        assertNull(inventory.getInventorySlots()[0]);
    }

    @Test
    void testSetInvalidIndex(){
        Inventory inventory = new Inventory();
        inventory.setActiveIndex(10);
        assertEquals(0, inventory.getActiveIndex());
    }

    @Test
    void testActiveItemNullIfEmpty(){
        Inventory inventory = new Inventory();
        assertNull(inventory.getActiveItem());
    }
}
