package cz.cvut.fel.pjv.labyrinthian.UI;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Core.GameState;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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


    public void showItemDialog(Item item) {
        hideAll();
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());
        setVisible(itemContent, true);
        setVisible(continueButton, true);
        dialogRoot.setVisible(true);
        gameManager.setCurrentState(GameState.DIALOGUE);
    }

    public void showObliteratorDialog(Item item) {
        hideAll();
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());
        setVisible(itemContent, true);
        setVisible(obliteratorWarning, true);
        dialogRoot.setVisible(true);
        gameManager.setCurrentState(GameState.DIALOGUE);
    }

    public void showUpgradeDialog() {
        hideAll();
        setVisible(upgradeContent, true);
        dialogRoot.setVisible(true);
        gameManager.setCurrentState(GameState.DIALOGUE);
    }

    public void showNpcDialog() {
        hideAll();
        setVisible(npcContent, true);
        setVisible(continueButton, true);
        dialogRoot.setVisible(true);
        gameManager.setCurrentState(GameState.DIALOGUE);
    }


    @FXML
    public void onContinue() {
        dialogRoot.setVisible(false);
        gameManager.setCurrentState(GameState.RUNNING);
    }

    @FXML
    public void onEquip() {
        dialogRoot.setVisible(false);
        gameManager.getPendingPickup().onInteraction(gameManager.getMainCharacter(), gameManager);
        gameManager.setPendingPickup(null);
        gameManager.setCurrentState(GameState.RUNNING);
    }

    @FXML
    public void onNevermind() {
        dialogRoot.setVisible(false);
        gameManager.setCurrentState(GameState.RUNNING);
    }

    @FXML
    public void onUpgradeHealth() {
        dialogRoot.setVisible(false);
        gameManager.getMainCharacter().setMaxHealth(gameManager.getMainCharacter().getMaxHealth() + 2);
        gameManager.getMainCharacter().setDeafaultValues(gameManager.getMainCharacter().getMaxHealth(), 0);
        gameManager.getMainCharacter().heal(gameManager.getMainCharacter().getMaxHealth(), gameManager);
        gameManager.setCurrentState(GameState.RUNNING);
    }

    @FXML
    public void onUpgradeDamage() {
        dialogRoot.setVisible(false);
        gameManager.getMainCharacter().getActiveweapon().setDamage(
                (int) (gameManager.getMainCharacter().getActiveweapon().getDamage() * 2)
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