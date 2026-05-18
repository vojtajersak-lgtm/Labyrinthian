package cz.cvut.fel.pjv.labyrinthian.Core;

/**
 * Represents the current state of the game loop.
 * The AnimationTimer switches behavior based on this value each frame.
 */
public enum GameState {
    /** Player is in the main menu — game loop is not running. */
    MAIN_MENU,
    /** Game is actively running — update and render every frame. */
    RUNNING,
    /** Game is paused — pause menu overlay is shown. */
    PAUSED,
    /** A dialog screen is shown — game logic is suspended. */
    DIALOGUE,
    /** Player has died — transition to the end screen. */
    GAME_OVER,
    /** Player completed all 5 levels — transition to the win screen. */
    WON,
    /** Player exited through the escape portal — trigger next level. */
    LEVEL_COMPLETE,
    /** End screen is displayed — no further updates needed. */
    SHOWING_END
}
