package cz.cvut.fel.pjv.labyrinthian.Core;

import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages keyboard input state for the game.
 * <p>
 * Tracks three separate sets of keys
 *
 *  <li>{@code lastCode} — keys currently held down (for smooth WASD movement)
 *  <li>{@code justPressed} — keys fired only on the first press for single-action inputs
 *  <li>{@code justReleased} — keys released this frame (used to detect new presses after release)
 *
 */
public class InputManager {
    /** Keys fired only on the very first press (not while held). Cleared every frame. */
    Set<KeyCode> justPressed = new HashSet<>();
    /** All keys currently held down. */
    private final Set<KeyCode> lastCode = new HashSet<>();
    /** Keys released this frame. Cleared at the start of each key press event. */
    private final Set<KeyCode> justReleased = new HashSet<>();
    /** The last single key press (for switch-based one-off actions). */
    private KeyCode LastPressed;

    public Set<KeyCode> getJustReleased() { return justReleased; }

    /**
     * Adds a key to the justReleased set when it is released.
     *
     * @param justReleased the released key
     */
    public void setJustReleased(KeyCode justReleased) { this.justReleased.add(justReleased); }

    public Set<KeyCode> getJustPressed() { return justPressed; }
    public KeyCode getLastPressed() { return LastPressed; }
    public void setLastPressed(KeyCode lastPressed) { LastPressed = lastPressed; }
    public Set<KeyCode> getLastCode() { return lastCode; }

    /**
     * Registers a key press. Adds to {@code justPressed} only if the key was previously
     * released (preventing continuous firing while held).
     *
     * @param code the pressed key
     */
    public void setLastCode(KeyCode code) {
        // Key counts as "just pressed" only if it was released before or not yet tracked
        if (justReleased.contains(code) || !lastCode.contains(code)) {
            justPressed.add(code);
        }
        lastCode.add(code);
    }

    /**
     * Removes a key from the held-keys set when it is released.
     *
     * @param lastCode the released key to remove
     */
    public void removeLastCode(KeyCode lastCode) {
        this.lastCode.remove(lastCode);
    }
}
