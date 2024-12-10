package bricker.brick_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;

/**
 * The AddPuckStrategy class implements the CollisionStrategy interface and is responsible
 * for adding pucks to the game when a specific collision occurs. Upon collision, a new puck
 * is created at the location of the brick that was involved in the collision.
 */
public class AddPuckStrategy implements CollisionStrategy {
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs an AddPuckStrategy object.
     * @param brickerGameManager The game manager responsible for managing game objects and game state.
     */
    public AddPuckStrategy(BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision between two game objects. When a brick collides with another object,
     * it triggers the creation of pucks at the location of the brick and removes the brick from the game.
     * @param thisObj The game object that is part of the collision (usually a brick).
     * @param otherObj The other game object involved in the collision (typically the object triggering the collision).
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.createPucks(thisObj.getCenter());
        brickerGameManager.removeBrick(thisObj);
    }
}
