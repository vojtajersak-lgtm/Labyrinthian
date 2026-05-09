package cz.cvut.fel.pjv.labyrinthian.Core;

import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

public class InputManager {
    Set<KeyCode> justPressed = new HashSet<>();
    private Set<KeyCode> lastCode = new HashSet<>();
    private Set<KeyCode> justReleased = new HashSet<>();
    private KeyCode LastPressed;

    public Set<KeyCode> getJustReleased() {
        return justReleased;
    }

    public void setJustReleased(KeyCode justReleased) {
        this.justReleased.add(justReleased);
    }

    public Set<KeyCode> getJustPressed() {
        return justPressed;
    }

    public KeyCode getLastPressed() {
        return LastPressed;
    }

    public void setLastPressed(KeyCode lastPressed) {
        LastPressed = lastPressed;
    }

    public Set<KeyCode> getLastCode() {
        return lastCode;
    }

    public void setLastCode(KeyCode code) {
        if (justReleased.contains(code) || !lastCode.contains(code)) {
            justPressed.add(code);
        }
        lastCode.add(code);
    }

    public void removeLastCode(KeyCode lastCode) {
        this.lastCode.remove(lastCode);
    }
}

