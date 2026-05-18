package cz.cvut.fel.pjv.labyrinthian.UI;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Core.GameState;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for all dialogue  pop-ups in the game (item description, player upgrade, npc dialogue...)
 * shows/hdies the appropriate dialogue windows
 */
public class DialogScreen {

    @FXML
    private AnchorPane dialogRoot;
    @FXML
    private VBox itemContent;
    @FXML
    private VBox obliteratorWarning;
    @FXML
    private VBox upgradeContent;
    @FXML
    private VBox npcContent;
    @FXML
    private Button continueButton;
    @FXML
    private Label itemName;
    @FXML
    private Label itemDescription;


    private GameManager gameManager;

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Shows item name, description and lists item effects.
     * @param item Item that is described
     */
    public void showItemDialog(Item item) {
        hideAll();
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());
        setVisible(itemContent, true);
        setVisible(continueButton, true);
        dialogRoot.setVisible(true);
        gameManager.setCurrentState(GameState.DIALOGUE);
    }

    /**
     * Lists name, description and effects of the Ultimate obliterator
     * Asks player to confirm to equip the weapon
     * @param item the Ultimate obliterator
     */
    public void showObliteratorDialog(Item item) {
        hideAll();
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());
        setVisible(itemContent, true);
        setVisible(obliteratorWarning, true);
        dialogRoot.setVisible(true);
        gameManager.setCurrentState(GameState.DIALOGUE);
    }

    /**
     * Presents upgrade options to player upon killing standard enemy - extra heart/ more damage.
     */
    public void showUpgradeDialog() {
        hideAll();
        setVisible(upgradeContent, true);
        dialogRoot.setVisible(true);
        gameManager.setCurrentState(GameState.DIALOGUE);
    }

    /**
     * Npc thanks player after being transformed
     */
    public void showNpcDialog() {
        hideAll();
        setVisible(npcContent, true);
        setVisible(continueButton, true);
        dialogRoot.setVisible(true);
        gameManager.setCurrentState(GameState.DIALOGUE);
    }


    /**
     * closes dialogue window
     */
    @FXML
    public void onContinue() {
        dialogRoot.setVisible(false);
        gameManager.setCurrentState(GameState.RUNNING);
    }

    /**
     * Confirmation to equip the ultimate obliterator - equips it as the active weapon
     */
    @FXML
    public void onEquip() {
        dialogRoot.setVisible(false);
        gameManager.getPendingPickup().onInteraction(gameManager.getMainCharacter(), gameManager);
        gameManager.setPendingPickup(null);
        gameManager.setCurrentState(GameState.RUNNING);
    }

    /**
     * If player decides not to equip the ultimate obliterator, the weapon remains on the ground.
     */
    @FXML
    public void onNevermind() {
        dialogRoot.setVisible(false);
        gameManager.setCurrentState(GameState.RUNNING);
    }

    /**
     * Adds 1 heart to maximum health, heals player to full health
     */
    @FXML
    public void onUpgradeHealth() {
        dialogRoot.setVisible(false);
        gameManager.getMainCharacter().setMaxHealth(gameManager.getMainCharacter().getMaxHealth() + 2);
        gameManager.getMainCharacter().setDeafaultValues(gameManager.getMainCharacter().getMaxHealth(), 0);
        gameManager.getMainCharacter().heal(gameManager.getMainCharacter().getMaxHealth(), gameManager);
        gameManager.setCurrentState(GameState.RUNNING);
    }

    /**
     * adds (n^2)/10 damage to players damage. (n is current level number)
     */
    @FXML
    public void onUpgradeDamage() {
        dialogRoot.setVisible(false);
        gameManager.getMainCharacter().getActiveweapon().setDamage(
                 (gameManager.getMainCharacter().getActiveweapon().getDamage() + Math.pow(gameManager.getGamestats().getCurrentLevel(),2) /10)
        );
        gameManager.getMainCharacter().setDeafaultValues(gameManager.getMainCharacter().getActiveweapon().getDamage(), 2);

        gameManager.setCurrentState(GameState.RUNNING);
    }


    private void hideAll() {
        setVisible(itemContent, false);
        setVisible(obliteratorWarning, false);
        setVisible(upgradeContent, false);
        setVisible(npcContent, false);
        setVisible(continueButton, false);
    }

    private void setVisible(VBox node, boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }

    private void setVisible(Button node, boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }
}