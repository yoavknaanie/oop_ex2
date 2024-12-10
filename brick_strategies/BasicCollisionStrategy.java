package bricker.brick_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;

/**
 * The BasicCollisionStrategy class implements the CollisionStrategy interface and is responsible
 * for handling the basic collision behavior in the game. When a collision occurs, it removes
 * the brick involved in the collision from the game.
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs a BasicCollisionStrategy object.
     * @param brickerGameManager The game manager responsible for managing game objects and game state.
     */
    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision between two game objects. In this case, when a brick collides
     * with any other object, the brick is removed from the game.
     * @param thisObj The game object that is part of the collision (usually a brick).
     * @param otherObj The other game object involved in the collision (typically any object that causes the collision).
     */
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.removeBrick(thisObj);
    }
}
