package cz.cvut.fel.pjv.labyrinthian.Core;

import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

public class InputManager {
    private Set<KeyCode> lastCode = new HashSet<>();

    public Set<KeyCode> getLastCode() {
        return lastCode;
    }

    public void setLastCode(KeyCode lastCode) {
        this.lastCode.add(lastCode);
    }

    public void removeLastCode(KeyCode lastCode){
        this.lastCode.remove(lastCode);
    }
}

