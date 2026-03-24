package cz.cvut.fel.pjv.labyrinthian.Core;

import javafx.scene.input.KeyCode;

public class InputManager {
    private KeyCode lastCode;

    public KeyCode getLastCode() {
        return lastCode;
    }

    public void setLastCode(KeyCode lastCode) {
        this.lastCode = lastCode;
    }
}

