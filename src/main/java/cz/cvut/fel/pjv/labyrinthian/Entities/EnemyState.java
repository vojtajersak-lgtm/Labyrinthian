package cz.cvut.fel.pjv.labyrinthian.Entities;

/**
 * Represents the current AI state of an enemy.
 * The state machine transitions are handled in {@link Enemy#takeTurn}.
 */
public enum EnemyState {
    /** Enemy is at its spawn point and not aware of the player. */
    IDLE,
    /** Enemy has spotted the player and is actively pathfinding toward them. */
    CHASING,
    /** Enemy lost sight of the player and is returning to its spawn point. Enemy must reach its spawn point before engaging again */
    RETURN,
    /** Enemy is within attack range and dealing damage to the player. */
    ATTACKING
}
